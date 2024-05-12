package com.xiaoxi.common.model.invokecontext.paramtype;

import lombok.Data;

import java.util.List;

/**
 *  用于前端测试调用的参数解析
 */
@Data
public class RequestParam {

    private List<KeyValueParam> params;
}
