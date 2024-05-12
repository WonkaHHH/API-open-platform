package com.xiaoxi.xiapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxi.common.common.ErrorCode;
import com.xiaoxi.common.exception.BusinessException;
import com.xiaoxi.common.model.entity.InterfaceInfo;
import com.xiaoxi.xiapi.mapper.InterfaceInfoMapper;
import com.xiaoxi.xiapi.service.InterfaceInfoService;
import com.xiaoxi.xiapi.service.invokeservice.InvokeService;
import com.xiaoxi.xiapi.service.invokeservice.impl.InvokeGPTAIService;
import com.xiaoxi.xiapi.service.invokeservice.impl.InvokeNameService;
import com.xiaoxi.xiapi.service.invokeservice.impl.InvokeSparkAIService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceinfo, boolean add) {
        if (interfaceinfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String name = interfaceinfo.getName();
        String description = interfaceinfo.getDescription();
        String host = interfaceinfo.getHost();
        String url = interfaceinfo.getUrl();
        String requestparams = interfaceinfo.getRequestParams();
        String requestheader = interfaceinfo.getRequestHeader();
        String responseheader = interfaceinfo.getResponseHeader();
        String method = interfaceinfo.getMethod();

        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(name, description, url, requestparams, requestheader, responseheader, method)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }

        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名字过长");
        }

        if (StringUtils.isNotBlank(description) && description.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "描述过长");
        }

        if (StringUtils.isNotBlank(host) && url.length() > 256) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "host过长");
        }

        if (StringUtils.isNotBlank(url) && url.length() > 256) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "url过长");
        }

        if (StringUtils.isNotBlank(requestparams) && requestparams.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数过长");
        }

        if (StringUtils.isNotBlank(requestheader) && requestheader.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求头过长");
        }

        if (StringUtils.isNotBlank(responseheader) && responseheader.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "相应头过长");
        }

        if (StringUtils.isNotBlank(method) && method.length() > 256) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求方法过长");
        }
    }

    // 接口id对应接口服务类的Map
    private static final Map<String, InvokeService> serviceMap = new HashMap<>();

    // key换成枚举
    static {
        serviceMap.put(InterfaceInfoIdEnum.GetUserName.getValue(), new InvokeNameService());
        serviceMap.put(InterfaceInfoIdEnum.DoChatByGPT.getValue(), new InvokeGPTAIService());
        serviceMap.put(InterfaceInfoIdEnum.DoChatBySpark.getValue(), new InvokeSparkAIService());
    }

    // 使用工厂+策略模式根据不同的接口id调用不同的接口
    @Override
    public Object invokeInterface(String accessKey, String secretKey, long id, String userRequestParams) {
        return serviceMap.get(String.valueOf(id)).invokeInterface(accessKey, secretKey, userRequestParams);
    }

    enum InterfaceInfoIdEnum {

        GetUserName("GetUserName", "1"),
        DoChatByGPT("DoChatByGPT", "3"),
        DoChatBySpark("DoChatBySpark", "2");

        private final String text;

        private final String value;

        InterfaceInfoIdEnum(String text, String value) {
            this.text = text;
            this.value = value;
        }

        /**
         * 获取值列表
         *
         * @return
         */
        public static List<String> getValues() {
            return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
        }

        /**
         * 根据 value 获取枚举
         *
         * @param value
         * @return
         */
        public static InterfaceInfoIdEnum getEnumByValue(String value) {
            if (ObjectUtils.isEmpty(value)) {
                return null;
            }
            for (InterfaceInfoIdEnum anEnum : InterfaceInfoIdEnum.values()) {
                if (anEnum.value.equals(value)) {
                    return anEnum;
                }
            }
            return null;
        }

        public String getValue() {
            return value;
        }

        public String getText() {
            return text;
        }
    }
}




