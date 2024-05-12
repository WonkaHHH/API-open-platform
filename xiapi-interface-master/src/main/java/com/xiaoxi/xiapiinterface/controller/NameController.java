package com.xiaoxi.xiapiinterface.controller;

import cn.hutool.core.util.StrUtil;
import com.xiaoxi.common.model.invokecontext.request.NameRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/name")
public class NameController{

    @PostMapping("/get")
    public String getUserName(@RequestBody NameRequest request) {
        if (request == null){
            return "请求参数为空";
        }
        String userName = request.getUserName();
        if (StrUtil.isBlank(userName)){
            return "请求参数为空";
        }
        return "这是一个测试，你的用户名是:" + request.getUserName();
    }

}

