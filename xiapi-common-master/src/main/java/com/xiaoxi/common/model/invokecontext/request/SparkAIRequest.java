package com.xiaoxi.common.model.invokecontext.request;

import lombok.Data;

@Data
public class SparkAIRequest {

    /**
     * 设置全局自定义提示词(用于对聊天模型进行角色设置，如:现在开始你是一位高级程序员,请从专业角度回答我的问题)
     * 可以为空
     */
    private String globalMessage;

    // 提问信息
    private String userMessage;
}
