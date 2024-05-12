package com.xiaoxi.xiapiinterface.controller;

import cn.hutool.core.util.StrUtil;
import com.xiaoxi.common.model.invokecontext.request.GPTAIRequest;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/gpt_ai")
public class GPTAIController {

    @Resource
    private YuCongMingClient client;

    @PostMapping("dochat")
    public String doChatByGPT(@RequestBody GPTAIRequest request){
        if (request == null){
            return "请求参数为空";
        }
        String message = request.getMessage();
        if (StrUtil.isBlank(message)){
            return "请求参数为空";
        }

        DevChatRequest devChatRequest = new DevChatRequest();
        devChatRequest.setModelId(1654785040361893889L);
        devChatRequest.setMessage(message);

        BaseResponse<DevChatResponse> response = client.doChat(devChatRequest);

        if (response.getCode() == 0){
            return response.getData().getContent();
        }

        return response.getMessage();
    }
}
