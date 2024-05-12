package com.xiaoxi.xiapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoxi.common.common.BaseResponse;
import com.xiaoxi.common.common.DeleteRequest;
import com.xiaoxi.common.common.ErrorCode;
import com.xiaoxi.common.common.ResultUtils;
import com.xiaoxi.common.constant.CommonConstant;
import com.xiaoxi.common.constant.UserConstant;
import com.xiaoxi.common.exception.BusinessException;
import com.xiaoxi.common.model.vo.UserInterfaceInfoVO;
import com.xiaoxi.xiapi.annotation.AuthCheck;
import com.xiaoxi.xiapi.model.dto.userinterfaceinfo.UserInterfaceInfoAddRequest;
import com.xiaoxi.xiapi.model.dto.userinterfaceinfo.UserInterfaceInfoQueryRequest;
import com.xiaoxi.xiapi.model.dto.userinterfaceinfo.UserInterfaceInfoUpdateRequest;
import com.xiaoxi.common.model.entity.User;
import com.xiaoxi.common.model.entity.UserInterfaceInfo;
import com.xiaoxi.xiapi.service.InterfaceInfoService;
import com.xiaoxi.xiapi.service.UserInterfaceInfoService;
import com.xiaoxi.xiapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户接口关系
 */
@RestController
@RequestMapping("/userinterfaceinfo")
@Slf4j
public class UserInterfaceInfoController {

    @Resource
    private UserInterfaceInfoService userinterfaceinfoService;

    @Resource
    private UserService userService;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     * 创建
     *
     * @param userInterfaceInfoAddRequest
     * @param request
     * @return
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/add")
    public BaseResponse<Long> addUserInterfaceInfo(@RequestBody UserInterfaceInfoAddRequest userInterfaceInfoAddRequest, HttpServletRequest request) {
        if (userInterfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo interfaceinfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoAddRequest, interfaceinfo);
        // 校验
        userinterfaceinfoService.validUserInterfaceInfo(interfaceinfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceinfo.setUserId(loginUser.getId());
        boolean result = userinterfaceinfoService.save(interfaceinfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newUserInterfaceInfoId = interfaceinfo.getId();
        return ResultUtils.success(newUserInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUserInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        UserInterfaceInfo oldUserInterfaceInfo = userinterfaceinfoService.getById(id);
        if (oldUserInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldUserInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = userinterfaceinfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param userInterfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUserInterfaceInfo(@RequestBody UserInterfaceInfoUpdateRequest userInterfaceInfoUpdateRequest,
                                            HttpServletRequest request) {
        if (userInterfaceInfoUpdateRequest == null || userInterfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo interfaceinfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoUpdateRequest, interfaceinfo);
        // 参数校验
        userinterfaceinfoService.validUserInterfaceInfo(interfaceinfo, false);
        User user = userService.getLoginUser(request);
        long id = userInterfaceInfoUpdateRequest.getId();
        // 判断是否存在
        UserInterfaceInfo oldUserInterfaceInfo = userinterfaceinfoService.getById(id);
        if (oldUserInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldUserInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = userinterfaceinfoService.updateById(interfaceinfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @GetMapping("/get")
    public BaseResponse<UserInterfaceInfo> getUserInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo interfaceinfo = userinterfaceinfoService.getById(id);
        return ResultUtils.success(interfaceinfo);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @GetMapping("/list")
    public BaseResponse<List<UserInterfaceInfo>> listUserInterfaceInfo(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        UserInterfaceInfo interfaceinfoQuery = new UserInterfaceInfo();
        if (userInterfaceInfoQueryRequest != null) {
            BeanUtils.copyProperties(userInterfaceInfoQueryRequest, interfaceinfoQuery);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceinfoQuery);
        List<UserInterfaceInfo> interfaceinfoList = userinterfaceinfoService.list(queryWrapper);
        return ResultUtils.success(interfaceinfoList);
    }

    /**
     * 分页获取列表
     *
     * @param interfaceinfoQueryRequest
     * @param request
     * @return
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @GetMapping("/list/page")
    public BaseResponse<Page<UserInterfaceInfo>> listUserInterfaceInfoByPage(UserInterfaceInfoQueryRequest interfaceinfoQueryRequest, HttpServletRequest request) {
        if (interfaceinfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo interfaceinfoQuery = new UserInterfaceInfo();
        BeanUtils.copyProperties(interfaceinfoQueryRequest, interfaceinfoQuery);
        long current = interfaceinfoQueryRequest.getCurrent();
        long size = interfaceinfoQueryRequest.getPageSize();
        String sortField = interfaceinfoQueryRequest.getSortField();
        String sortOrder = interfaceinfoQueryRequest.getSortOrder();
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceinfoQuery);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<UserInterfaceInfo> interfaceinfoPage = userinterfaceinfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(interfaceinfoPage);
    }

    @GetMapping("/getByUserId")
    public BaseResponse<List<UserInterfaceInfoVO>> getByUserId(HttpServletRequest request){

        User loginUser = userService.getLoginUser(request);

        LambdaQueryWrapper<UserInterfaceInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInterfaceInfo::getUserId, loginUser.getId());
        List<UserInterfaceInfoVO> list = userinterfaceinfoService.list(queryWrapper).stream().map(userInterfaceInfo -> {
            UserInterfaceInfoVO userInterfaceInfoVO = new UserInterfaceInfoVO();
            String interfaceInfoName = interfaceInfoService.getById(userInterfaceInfo.getInterfaceInfoId()).getName();
            BeanUtils.copyProperties(userInterfaceInfo, userInterfaceInfoVO);
            userInterfaceInfoVO.setInterfaceInfoName(interfaceInfoName);

            return userInterfaceInfoVO;
        }).collect(Collectors.toList());


        return ResultUtils.success(list);
    }
}
