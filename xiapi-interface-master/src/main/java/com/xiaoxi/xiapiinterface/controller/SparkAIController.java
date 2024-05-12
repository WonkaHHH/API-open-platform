package com.xiaoxi.xiapiinterface.controller;

import cn.hutool.core.util.StrUtil;
import com.xiaoxi.common.model.invokecontext.request.SparkAIRequest;
import io.github.briqt.spark4j.SparkClient;
import io.github.briqt.spark4j.constant.SparkApiVersion;
import io.github.briqt.spark4j.exception.SparkException;
import io.github.briqt.spark4j.model.SparkMessage;
import io.github.briqt.spark4j.model.SparkSyncChatResponse;
import io.github.briqt.spark4j.model.request.SparkRequest;
import io.github.briqt.spark4j.model.response.SparkTextUsage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/spark_ai")
public class SparkAIController {

    @Resource
    private SparkClient sparkClient;

    @PostMapping("dochat")
    public String doChatBySpark(@RequestBody SparkAIRequest request) {
        if (request == null) {
            return "请求参数为空";
        }
        String globalMessage = request.getGlobalMessage();
        String userMessage = request.getUserMessage();
        if (StrUtil.isBlank(userMessage)) {
            return "请求参数为空";
        }

        // 消息列表，可以在此列表添加历史对话记录
        List<SparkMessage> messages = new ArrayList<>();
        if (!StrUtil.isBlank(globalMessage)) {
            messages.add(SparkMessage.systemContent(globalMessage));
        }
        messages.add(SparkMessage.userContent(userMessage));
        // 构造请求
        SparkRequest sparkRequest = SparkRequest.builder()
                // 消息列表
                .messages(messages)
                // 模型回答的tokens的最大长度,非必传，默认为2048。
                // V1.5取值为[1,4096]
                // V2.0取值为[1,8192]
                // V3.0取值为[1,8192]
                .maxTokens(2048)
                // 核采样阈值。用于决定结果随机性,取值越高随机性越强即相同的问题得到的不同答案的可能性越高 非必传,取值为[0,1],默认为0.5
                .temperature(0.2)
                // 指定请求版本，默认使用最新3.0版本
                .apiVersion(SparkApiVersion.V3_0)
                .build();

        String responseContent = null;
        try {
            // 同步调用
            SparkSyncChatResponse chatResponse = sparkClient.chatSync(sparkRequest);
            SparkTextUsage textUsage = chatResponse.getTextUsage();

            responseContent = chatResponse.getContent();
            log.info("\nSpark回答：" + responseContent);
            log.info("\nSpark提问tokens：" + textUsage.getPromptTokens()
                    + "，回答tokens：" + textUsage.getCompletionTokens()
                    + "，总消耗tokens：" + textUsage.getTotalTokens());
        } catch (SparkException e) {
            log.error("Spark发生异常了：" + e.getMessage());
        }
        return responseContent;
    }

}
