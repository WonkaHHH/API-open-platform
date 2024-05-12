package com.xiaoxi.common.model.invokecontext.response;

import lombok.Data;

@Data
public class ApiResponse {

    // 响应code
    private Integer code;

    // 响应数据主体
    private String data;

    // 响应信息(例如异常信息)
    private String message;
}
