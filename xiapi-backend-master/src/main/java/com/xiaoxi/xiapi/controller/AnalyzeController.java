package com.xiaoxi.xiapi.controller;

import com.xiaoxi.common.common.BaseResponse;
import com.xiaoxi.common.common.ResultUtils;
import com.xiaoxi.common.model.entity.User;
import com.xiaoxi.xiapi.model.vo.analyze.CategoryDataType;
import com.xiaoxi.xiapi.model.vo.analyze.PieDataType;
import com.xiaoxi.xiapi.model.vo.analyze.StatisticDataType;
import com.xiaoxi.xiapi.service.AnalyzeService;
import com.xiaoxi.xiapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/analyze")
@Slf4j
public class AnalyzeController {

    @Resource
    private UserService userService;

    @Resource
    private AnalyzeService analyzeService;

    @GetMapping("/getPerInterfaceCount")
    public BaseResponse<List<PieDataType>> getPerInterfaceCount(HttpServletRequest request){

        User loginUser = userService.getLoginUser(request);

        List<PieDataType> perInterfaceCount = analyzeService.getPerInterfaceCount(loginUser.getId());
        return ResultUtils.success(perInterfaceCount);
    }

    @GetMapping("/getPerInterfaceLeftCount")
    public BaseResponse<List<PieDataType>> getPerInterfaceLeftCount(HttpServletRequest request){

        User loginUser = userService.getLoginUser(request);

        List<PieDataType> perInterfaceLeftCount = analyzeService.getPerInterfaceLeftCount(loginUser.getId());
        return ResultUtils.success(perInterfaceLeftCount);
    }

    @GetMapping("/getInterfaceInvokeCountFor7")
    public BaseResponse<CategoryDataType> getInterfaceInvokeCountFor7(HttpServletRequest request){

        User loginUser = userService.getLoginUser(request);
        CategoryDataType result = analyzeService.getInterfaceInvokeCountFor7(loginUser.getId());
        return ResultUtils.success(result);
    }

    @GetMapping("/getStatistic")
    public BaseResponse<StatisticDataType> getStatistic(HttpServletRequest request){

        User loginUser = userService.getLoginUser(request);
        StatisticDataType result = analyzeService.getStatistic(loginUser.getId());
        return ResultUtils.success(result);
    }
}
