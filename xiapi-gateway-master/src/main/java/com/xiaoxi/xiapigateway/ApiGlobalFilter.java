package com.xiaoxi.xiapigateway;

import com.xiaoxi.common.model.entity.InterfaceInfo;
import com.xiaoxi.common.model.entity.User;
import com.xiaoxi.common.service.InnerInterfaceInfoService;
import com.xiaoxi.common.service.InnerUserInterfaceInfoService;
import com.xiaoxi.common.service.InnerUserService;
import com.xiaoxi.xiapiclientsdk.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class ApiGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerInterfaceInfoService interfaceInfoService;

    @DubboReference
    private InnerUserInterfaceInfoService userInterfaceInfoService;

    @DubboReference
    private InnerUserService userService;

    public static final List<String> IP_WRITE_LIST = Arrays.asList("127.0.0.1");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //用户发送请求到 API 网关
        //请求日志
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求唯一标识:" + request.getId());
        log.info("请求路径:" + request.getPath().value());
        log.info("请求方法:" + request.getMethod());
        log.info("请求参数:" + request.getQueryParams());
        log.info("请求远程地址:" + request.getRemoteAddress());
        String hostString = request.getLocalAddress().getHostString();
        log.info("请求来源地址" + hostString);
        // 访问控制（黑白名单）
        ServerHttpResponse response = exchange.getResponse();
        if (!IP_WRITE_LIST.contains(hostString)) {
            return handleNoAuth(response);
        }
        //用户鉴权（判断 ak、sk 是否合法）
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");
        String decodeBody = "";
        try {
            decodeBody = URLDecoder.decode(body, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return handleNoAuth(response);
        }
        // 去数据库中查询这个accessKey是否分配给用户

        User invokeUser = null;
        try {
            invokeUser = userService.getInvokeUser(accessKey);
        }catch (Exception e){
            log.error("getInvokeUser ERROR:", e);
        }

        if (invokeUser.getAccessKey() == null || invokeUser.getSecretKey() == null || StringUtils.isAnyBlank(invokeUser.getAccessKey(), invokeUser.getSecretKey())){
            return handleNoAuth(response);
        }

        // TODO nonce使用后存入hashMap或者redis，判断是否使用过
        if (Long.parseLong(nonce) > 100000) {
            return handleNoAuth(response);
        }
        // 时间戳格式化，判断是否超过当前时间五分钟
        if (new Date(Long.parseLong(timestamp) + 5 * 60 * 1000).before(new Date())) {
            return handleNoAuth(response);
        }
        // 传过来的签名与用相同签名算法生成的签名是否一致
        if (!SignUtils.genSign(decodeBody, invokeUser.getSecretKey()).equals(sign)) {
            return handleNoAuth(response);
        }
        //请求的模拟接口是否存在？
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = interfaceInfoService.getInterfaceInfo(request.getPath().value(), request.getMethod().toString());
        } catch (Exception e) {
            log.error("getInterfaceInfo error", e);
        }
        if (interfaceInfo == null) {
            return handleNoAuth(response);
        }

        // todo 是否还有调用次数
        int leftNum = userInterfaceInfoService.getLeftNum(interfaceInfo.getId(), invokeUser.getId());
        if (leftNum <= 0){
            return handleInvokeNoLeftError(response);
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口调用次数已为0!");
        }
        // 5. 请求转发，调用模拟接口
        // Mono<Void> filter = chain.filter(exchange);
        // 6. 响应日志
        return handleResponse(exchange, chain, interfaceInfo.getId(), invokeUser.getId(), response);
    }

    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceId, long userId, ServerHttpResponse response) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();

            HttpStatus statusCode = originalResponse.getStatusCode();

            if (statusCode == HttpStatus.OK) {
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {

                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            //
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                // 7.  调用成功，接口调用次数+1 invokeCount
                                try {
                                    userInterfaceInfoService.invokeCount(interfaceId, userId);
                                } catch (Exception e) {
                                    log.error("invokeCount error", e);
                                }
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);//释放掉内存
                                // 构建日志
                                StringBuilder sb2 = new StringBuilder(200);
                                String data = new String(content, StandardCharsets.UTF_8);//data
                                sb2.append(data);
                                // 打印日志
                                log.info("响应体->" + data);
                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);//降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应异常：" + e);
            return handleInvokeError(response);
//            return chain.filter(exchange);
        }
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    private Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }

    private Mono<Void> handleInvokeNoLeftError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.NOT_FOUND);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}