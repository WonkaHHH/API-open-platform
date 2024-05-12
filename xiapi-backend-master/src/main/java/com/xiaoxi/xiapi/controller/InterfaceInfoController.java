package com.xiaoxi.xiapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoxi.common.common.*;
import com.xiaoxi.common.constant.CommonConstant;
import com.xiaoxi.common.exception.BusinessException;
import com.xiaoxi.common.model.entity.InterfaceInfo;
import com.xiaoxi.common.model.entity.User;
import com.xiaoxi.common.model.enums.InterfaceInfoStatusEnum;
import com.xiaoxi.common.model.vo.SelectOptionVO;
import com.xiaoxi.xiapi.annotation.AuthCheck;
import com.xiaoxi.xiapi.annotation.MustLogin;
import com.xiaoxi.xiapi.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.xiaoxi.xiapi.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import com.xiaoxi.xiapi.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.xiaoxi.xiapi.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.xiaoxi.xiapi.service.InterfaceInfoService;
import com.xiaoxi.xiapi.service.UserService;
import com.xiaoxi.xiapiclientsdk.client.ApiClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 接口信息接口
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceinfoService;

    @Resource
    private UserService userService;

    /**
     * 创建
     *
     * @param interfaceinfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceinfoAddRequest, HttpServletRequest request) {
        if (interfaceinfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceinfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceinfoAddRequest, interfaceinfo);
        // 校验
        interfaceinfoService.validInterfaceInfo(interfaceinfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceinfo.setUserId(loginUser.getId());
        boolean result = interfaceinfoService.save(interfaceinfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newInterfaceInfoId = interfaceinfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceinfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceinfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param interfaceinfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceinfoUpdateRequest,
                                            HttpServletRequest request) {
        if (interfaceinfoUpdateRequest == null || interfaceinfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceinfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceinfoUpdateRequest, interfaceinfo);
        // 参数校验
        interfaceinfoService.validInterfaceInfo(interfaceinfo, false);
        User user = userService.getLoginUser(request);
        long id = interfaceinfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceinfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = interfaceinfoService.updateById(interfaceinfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceinfo = interfaceinfoService.getById(id);
        return ResultUtils.success(interfaceinfo);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param interfaceinfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<InterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryRequest interfaceinfoQueryRequest) {
        InterfaceInfo interfaceinfoQuery = new InterfaceInfo();
        if (interfaceinfoQueryRequest != null) {
            BeanUtils.copyProperties(interfaceinfoQueryRequest, interfaceinfoQuery);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceinfoQuery);
        List<InterfaceInfo> interfaceinfoList = interfaceinfoService.list(queryWrapper);
        return ResultUtils.success(interfaceinfoList);
    }

    /**
     * 分页获取列表
     *
     * @param interfaceinfoQueryRequest
     * @param request
     * @return
     */
    @MustLogin
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceinfoQueryRequest, HttpServletRequest request) {
        if (interfaceinfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceinfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceinfoQueryRequest, interfaceinfoQuery);
        long current = interfaceinfoQueryRequest.getCurrent();
        long size = interfaceinfoQueryRequest.getPageSize();
        String sortField = interfaceinfoQueryRequest.getSortField();
        String sortOrder = interfaceinfoQueryRequest.getSortOrder();
        String description = interfaceinfoQuery.getDescription();
        // description 需支持模糊搜索
        interfaceinfoQuery.setDescription(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceinfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<InterfaceInfo> interfaceinfoPage = interfaceinfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(interfaceinfoPage);
    }

    /**
     * 上线
     *
     * @param idRequest
     * @param request
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @PostMapping("/online")
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest,
                                                     HttpServletRequest request) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = idRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceinfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 仅本人或管理员可修改
        if (!oldInterfaceInfo.getUserId().equals(userService.getLoginUser(request).getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        InterfaceInfo interfaceinfo = new InterfaceInfo();
        interfaceinfo.setId(id);
        interfaceinfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        boolean isSuccess = interfaceinfoService.updateById(interfaceinfo);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 下线
     *
     * @param idRequest
     * @param request
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @PostMapping("/offline")
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest,
                                                     HttpServletRequest request) {
            if (idRequest == null || idRequest.getId() <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            long id = idRequest.getId();
            // 判断是否存在
            InterfaceInfo oldInterfaceInfo = interfaceinfoService.getById(id);
            if (oldInterfaceInfo == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
            }

            // 仅本人或管理员可修改
            if (!oldInterfaceInfo.getUserId().equals(userService.getLoginUser(request).getId()) && !userService.isAdmin(request)) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
            InterfaceInfo interfaceinfo = new InterfaceInfo();
            interfaceinfo.setId(id);
            interfaceinfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
            boolean isSuccess = interfaceinfoService.updateById(interfaceinfo);
            return ResultUtils.success(isSuccess);
    }

    /**
     * 调用
     *
     * @param interfaceInfoInvokeRequest
     * @param request
     * @return
     */
    @PostMapping("/invoke")
    public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest,
                                                      HttpServletRequest request) {
        if (interfaceInfoInvokeRequest == null || interfaceInfoInvokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = interfaceInfoInvokeRequest.getId();
        String userRequestParams = interfaceInfoInvokeRequest.getUserRequestParams();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceinfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "接口不存在");
        }
        // 判断接口是否关闭
        if (oldInterfaceInfo.getStatus() == InterfaceInfoStatusEnum.OFFLINE.getValue()){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口已关闭");
        }
        // 调用接口
        User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        // TODO 根据接口地址调用接口
        Object result = interfaceinfoService.invokeInterface(accessKey, secretKey, id, userRequestParams);

        return ResultUtils.success(result);
    }

    @GetMapping("getInterfaceIdAndName")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<List<SelectOptionVO>> getInterfaceIdAndName(HttpServletRequest request){

        List<SelectOptionVO> interfaceInfoIdAndNameList = interfaceinfoService.list().stream().map(interfaceInfo -> {
            SelectOptionVO selectOptionVO = new SelectOptionVO();
            selectOptionVO.setLabel(interfaceInfo.getName());
            selectOptionVO.setValue(interfaceInfo.getId());

            return selectOptionVO;
        }).collect(Collectors.toList());

        return ResultUtils.success(interfaceInfoIdAndNameList);
    }
}
