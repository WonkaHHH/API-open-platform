package com.xiaoxi.xiapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoxi.common.common.*;
import com.xiaoxi.common.constant.CommonConstant;
import com.xiaoxi.common.exception.BusinessException;
import com.xiaoxi.common.model.entity.InterfaceInfoOrder;
import com.xiaoxi.common.model.entity.User;
import com.xiaoxi.common.model.vo.InterfaceInfoMyOrderVO;
import com.xiaoxi.xiapi.annotation.AuthCheck;
import com.xiaoxi.xiapi.model.dto.interfaceinfoorder.InterfaceInfoOrderAddRequest;
import com.xiaoxi.xiapi.model.dto.interfaceinfoorder.InterfaceInfoOrderPayRequest;
import com.xiaoxi.xiapi.model.dto.interfaceinfoorder.InterfaceInfoOrderQueryRequest;
import com.xiaoxi.xiapi.model.dto.interfaceinfoorder.InterfaceInfoOrderUpdateRequest;
import com.xiaoxi.xiapi.service.InterfaceInfoOrderService;
import com.xiaoxi.xiapi.service.InterfaceInfoProductService;
import com.xiaoxi.xiapi.service.InterfaceInfoService;
import com.xiaoxi.xiapi.service.UserService;
import com.xiaoxi.xiapi.utils.SnowFlakeCompone;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 接口订单接口
 */
@RestController
@RequestMapping("/interfaceInfoOrder")
@Slf4j
public class InterfaceInfoOrderController {

    @Resource
    private InterfaceInfoOrderService interfaceInfoOrderService;

    @Resource
    private UserService userService;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private InterfaceInfoProductService interfaceInfoProductService;

    /**
     * 创建
     *
     * @param interfaceInfoOrderAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfoOrder(@RequestBody InterfaceInfoOrderAddRequest interfaceInfoOrderAddRequest, HttpServletRequest request) {
        if (interfaceInfoOrderAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        long newInterfaceInfoOrderId = interfaceInfoOrderService.addOrder(interfaceInfoOrderAddRequest, loginUser.getId());

        return ResultUtils.success(newInterfaceInfoOrderId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfoOrder(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfoOrder oldInterfaceInfoOrder = interfaceInfoOrderService.getById(id);
        if (oldInterfaceInfoOrder == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldInterfaceInfoOrder.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceInfoOrderService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param interfaceInfoOrderUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfoOrder(@RequestBody InterfaceInfoOrderUpdateRequest interfaceInfoOrderUpdateRequest,
                                            HttpServletRequest request) {
        if (interfaceInfoOrderUpdateRequest == null || interfaceInfoOrderUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfoOrder interfaceInfo = new InterfaceInfoOrder();
        BeanUtils.copyProperties(interfaceInfoOrderUpdateRequest, interfaceInfo);
        // 参数校验
        interfaceInfoOrderService.validInterfaceInfoOrder(interfaceInfo, false);
        User user = userService.getLoginUser(request);
        long id = interfaceInfoOrderUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfoOrder oldInterfaceInfoOrder = interfaceInfoOrderService.getById(id);
        if (oldInterfaceInfoOrder == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldInterfaceInfoOrder.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = interfaceInfoOrderService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceInfoOrder> getInterfaceInfoOrderById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfoOrder interfaceInfo = interfaceInfoOrderService.getById(id);
        return ResultUtils.success(interfaceInfo);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param interfaceInfoOrderQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<InterfaceInfoOrder>> listInterfaceInfoOrder(InterfaceInfoOrderQueryRequest interfaceInfoOrderQueryRequest) {
        InterfaceInfoOrder interfaceInfoQuery = new InterfaceInfoOrder();
        if (interfaceInfoOrderQueryRequest != null) {
            BeanUtils.copyProperties(interfaceInfoOrderQueryRequest, interfaceInfoQuery);
        }
        QueryWrapper<InterfaceInfoOrder> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        List<InterfaceInfoOrder> interfaceInfoList = interfaceInfoOrderService.list(queryWrapper);
        return ResultUtils.success(interfaceInfoList);
    }

    /**
     * 分页获取列表
     *
     * @param interfaceInfoOrderQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfoOrder>> listInterfaceInfoOrderByPage(InterfaceInfoOrderQueryRequest interfaceInfoOrderQueryRequest, HttpServletRequest request) {
        if (interfaceInfoOrderQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfoOrder interfaceInfoQuery = new InterfaceInfoOrder();
        BeanUtils.copyProperties(interfaceInfoOrderQueryRequest, interfaceInfoQuery);
        long current = interfaceInfoOrderQueryRequest.getCurrent();
        long size = interfaceInfoOrderQueryRequest.getPageSize();
        String sortField = interfaceInfoOrderQueryRequest.getSortField();
        String sortOrder = interfaceInfoOrderQueryRequest.getSortOrder();
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfoOrder> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<InterfaceInfoOrder> interfaceInfoPage = interfaceInfoOrderService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(interfaceInfoPage);
    }

    /**
     *  支付
     * @param interfaceInfoOrderPayRequest
     * @param request
     * @return
     */
    @PostMapping("pay")
    public BaseResponse<Long> payInterfaceInfoOrder(@RequestBody InterfaceInfoOrderPayRequest interfaceInfoOrderPayRequest, HttpServletRequest request){
        Long userId = userService.getLoginUser(request).getId();

        Long orderId = interfaceInfoOrderService.payOrder(interfaceInfoOrderPayRequest, userId);

        return ResultUtils.success(orderId);
    }

    @GetMapping("/getMyOrderPage")
    public BaseResponse<Page<InterfaceInfoMyOrderVO>> getMyOrderPage(PageRequest pageRequest, HttpServletRequest request) {
        if (pageRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = pageRequest.getCurrent();
        long size = pageRequest.getPageSize();
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long userId = userService.getLoginUser(request).getId();
        LambdaQueryWrapper<InterfaceInfoOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InterfaceInfoOrder::getUserId, userId);

        Page<InterfaceInfoOrder> interfaceInfoPage = interfaceInfoOrderService.page(new Page<>(current, size), queryWrapper);

        List<InterfaceInfoMyOrderVO> interfaceInfoMyOrderVOList = interfaceInfoPage.getRecords().stream().map(interfaceInfoOrder -> {
            InterfaceInfoMyOrderVO interfaceInfoMyOrderVO = new InterfaceInfoMyOrderVO();
            BeanUtils.copyProperties(interfaceInfoOrder, interfaceInfoMyOrderVO);
            String interfaceInfoName = interfaceInfoService.getById(interfaceInfoOrder.getInterfaceInfoId()).getName();
            interfaceInfoMyOrderVO.setInterfaceInfoName(interfaceInfoName);
            Integer specification = interfaceInfoProductService.getById(interfaceInfoOrder.getInterfaceInfoProductId()).getSpecification();
            interfaceInfoMyOrderVO.setSpecification(specification);

            return interfaceInfoMyOrderVO;
        }).collect(Collectors.toList());

        Page<InterfaceInfoMyOrderVO> interfaceInfoMyOrderVOPage = new Page<>();
        BeanUtils.copyProperties(interfaceInfoPage, interfaceInfoMyOrderVOPage);
        interfaceInfoMyOrderVOPage.setRecords(interfaceInfoMyOrderVOList);

        return ResultUtils.success(interfaceInfoMyOrderVOPage);
    }
}
