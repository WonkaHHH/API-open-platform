package com.xiaoxi.xiapi.service.invokeservice;

import com.xiaoxi.common.model.invokecontext.response.ApiResponse;

public interface InvokeService {
    ApiResponse invokeInterface(String accessKey, String secretKey, String userRequestParams);
}
