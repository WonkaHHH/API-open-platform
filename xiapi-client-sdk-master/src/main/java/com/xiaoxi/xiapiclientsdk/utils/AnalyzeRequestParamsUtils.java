package com.xiaoxi.xiapiclientsdk.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.xiaoxi.common.common.ErrorCode;
import com.xiaoxi.common.exception.BusinessException;
import com.xiaoxi.common.model.invokecontext.paramtype.KeyValueParam;
import com.xiaoxi.common.model.invokecontext.paramtype.RequestParam;

import java.util.List;

/**
 *  将前端测试调用参数解析为List<KeyValueParam>待后续操作
 */
public class AnalyzeRequestParamsUtils {

    public static List<KeyValueParam> getParamList(String userRequestParams){
        if (StrUtil.isBlank(userRequestParams)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空或参数非法!");
        }
        RequestParam bean = JSONUtil.toBean(userRequestParams, RequestParam.class);
        return bean.getParams();
    }
}
