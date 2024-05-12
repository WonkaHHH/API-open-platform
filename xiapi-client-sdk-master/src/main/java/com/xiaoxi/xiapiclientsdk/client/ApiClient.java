package com.xiaoxi.xiapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.xiaoxi.common.model.invokecontext.enums.ResponseCodeEnum;
import com.xiaoxi.common.model.invokecontext.request.GPTAIRequest;
import com.xiaoxi.common.model.invokecontext.request.NameRequest;
import com.xiaoxi.common.model.invokecontext.request.SparkAIRequest;
import com.xiaoxi.common.model.invokecontext.response.ApiResponse;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static com.xiaoxi.xiapiclientsdk.utils.SignUtils.genSign;


public class ApiClient {

    private String accessKey;

    private String secretKey;

    public static final String GATEWAY_HOST = "http://127.0.0.1:8090";

    public ApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }


    private Map<String, String> getHeaderMap(String body) {
        String encodeBody = "";
        try {
            encodeBody = URLEncoder.encode(body, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("编码失败");
        }

        Map<String, String> header = new HashMap<>();
        header.put("accessKey", accessKey);
        header.put("nonce", RandomUtil.randomNumbers(5));
        header.put("body", encodeBody);
        header.put("timestamp", String.valueOf(System.currentTimeMillis()));
        header.put("sign", genSign(body, secretKey));
        return header;
    }

    public ApiResponse getUserName(NameRequest request) {
        String json = JSONUtil.toJsonStr(request);
        HttpResponse httpResponse = HttpRequest
                .post(GATEWAY_HOST + "/api/name/get")
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute();
        return getResponse(httpResponse);
    }

    public ApiResponse doChatByGPT(GPTAIRequest request) {
//        HashMap<String, Object> paramMap = new HashMap<>();
//        paramMap.put("question", question);

//        HttpResponse httpResponse = HttpRequest
//                .get(GATEWAY_HOST + "/api/ai/getanswer")
//                .addHeaders(getHeaderMap(question))
//                .form(paramMap).execute();

        String json = JSONUtil.toJsonStr(request);
        HttpResponse httpResponse = HttpRequest
                .post(GATEWAY_HOST + "/api/gpt_ai/dochat")
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute();

        return getResponse(httpResponse);
    }

    public ApiResponse doChatBySpark(SparkAIRequest request) {
        String json = JSONUtil.toJsonStr(request);
        HttpResponse httpResponse = HttpRequest
                .post(GATEWAY_HOST + "/api/spark_ai/dochat")
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute();

        return getResponse(httpResponse);
    }

    private static ApiResponse getResponse(HttpResponse httpResponse) {
        ApiResponse response = new ApiResponse();
        int responseStatus = httpResponse.getStatus();
        if (responseStatus != 200) {
            response.setCode(responseStatus);
            response.setMessage(ResponseCodeEnum.getEnumByValue(responseStatus).getText());
        }
        response.setCode(responseStatus);
        String body = httpResponse.body();
        response.setData(body);
        return response;
    }
}
