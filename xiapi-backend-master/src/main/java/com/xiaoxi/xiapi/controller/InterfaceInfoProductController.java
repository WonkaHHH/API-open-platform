package com.xiaoxi.xiapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoxi.common.common.BaseResponse;
import com.xiaoxi.common.common.DeleteRequest;
import com.xiaoxi.common.common.ErrorCode;
import com.xiaoxi.common.common.ResultUtils;
import com.xiaoxi.common.constant.CommonConstant;
import com.xiaoxi.common.exception.BusinessException;
import com.xiaoxi.common.model.entity.InterfaceInfoProduct;
import com.xiaoxi.common.model.vo.InterfaceInfoProductVO;
import com.xiaoxi.xiapi.annotation.AuthCheck;
import com.xiaoxi.xiapi.model.dto.interfaceinfoproduct.InterfaceInfoProductAddRequest;
import com.xiaoxi.xiapi.model.dto.interfaceinfoproduct.InterfaceInfoProductQueryRequest;
import com.xiaoxi.xiapi.model.dto.interfaceinfoproduct.InterfaceInfoProductUpdateRequest;
import com.xiaoxi.xiapi.service.InterfaceInfoProductService;
import com.xiaoxi.xiapi.service.InterfaceInfoService;
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
 * 接口商品接口
 */
@RestController
@RequestMapping("/InterfaceInfoProduct")
@Slf4j
public class InterfaceInfoProductController {

    @Resource
    private InterfaceInfoProductService interfaceInfoProductService;

    @Resource
    private UserService userService;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     * 创建
     *
     * @param interfaceInfoProductAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfoProduct(@RequestBody InterfaceInfoProductAddRequest interfaceInfoProductAddRequest, HttpServletRequest request) {
        synchronized (userService.getLoginUser(request).getUserAccount().intern()) {
            if (interfaceInfoProductAddRequest == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            InterfaceInfoProduct interfaceInfoProduct = new InterfaceInfoProduct();
            BeanUtils.copyProperties(interfaceInfoProductAddRequest, interfaceInfoProduct);
            // 校验
            interfaceInfoProductService.validInterfaceInfoProduct(interfaceInfoProduct, true);
            boolean result = interfaceInfoProductService.save(interfaceInfoProduct);
            if (!result) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR);
            }
            long newInterfaceInfoProductId = interfaceInfoProduct.getId();
            return ResultUtils.success(newInterfaceInfoProductId);
        }
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfoProduct(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfoProduct oldInterfaceInfoProduct = interfaceInfoProductService.getById(id);
        if (oldInterfaceInfoProduct == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 管理员可删除
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceInfoProductService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param interfaceInfoProductUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfoProduct(@RequestBody InterfaceInfoProductUpdateRequest interfaceInfoProductUpdateRequest,
                                                            HttpServletRequest request) {
        if (interfaceInfoProductUpdateRequest == null || interfaceInfoProductUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfoProduct interfaceInfoProduct = new InterfaceInfoProduct();
        BeanUtils.copyProperties(interfaceInfoProductUpdateRequest, interfaceInfoProduct);
        // 参数校验
        interfaceInfoProductService.validInterfaceInfoProduct(interfaceInfoProduct, false);
        long id = interfaceInfoProductUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfoProduct oldInterfaceInfoProduct = interfaceInfoProductService.getById(id);
        if (oldInterfaceInfoProduct == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅管理员可修改
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = interfaceInfoProductService.updateById(interfaceInfoProduct);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceInfoProductVO> getInterfaceInfoProductById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfoProduct interfaceInfoProduct = interfaceInfoProductService.getById(id);

        String interfaceInfoName = interfaceInfoService.getById(interfaceInfoProduct.getInterfaceInfoId()).getName();

        InterfaceInfoProductVO interfaceInfoProductVO = new InterfaceInfoProductVO();

        BeanUtils.copyProperties(interfaceInfoProduct, interfaceInfoProductVO);
        interfaceInfoProductVO.setInterfaceInfoName(interfaceInfoName);
        return ResultUtils.success(interfaceInfoProductVO);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param interfaceInfoProductQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<InterfaceInfoProductVO>> listInterfaceInfoProduct(InterfaceInfoProductQueryRequest interfaceInfoProductQueryRequest) {
        InterfaceInfoProduct interfaceInfoProductQuery = new InterfaceInfoProduct();
        if (interfaceInfoProductQueryRequest != null) {
            BeanUtils.copyProperties(interfaceInfoProductQueryRequest, interfaceInfoProductQuery);
        }
        QueryWrapper<InterfaceInfoProduct> queryWrapper = new QueryWrapper<>(interfaceInfoProductQuery);
        List<InterfaceInfoProductVO> interfaceInfoProductVOList = interfaceInfoProductService.list(queryWrapper).stream().map(interfaceInfoProduct -> {
            InterfaceInfoProductVO interfaceInfoProductVO = new InterfaceInfoProductVO();
            BeanUtils.copyProperties(interfaceInfoProduct, interfaceInfoProductVO);
            String interfaceInfoName = interfaceInfoService.getById(interfaceInfoProduct.getInterfaceInfoId()).getName();
            interfaceInfoProductVO.setInterfaceInfoName(interfaceInfoName);

            return interfaceInfoProductVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(interfaceInfoProductVOList);
    }

    /**
     * 分页获取列表
     *
     * @param interfaceInfoProductQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfoProductVO>> listInterfaceInfoProductByPage(InterfaceInfoProductQueryRequest interfaceInfoProductQueryRequest, HttpServletRequest request) {
        if (interfaceInfoProductQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfoProduct interfaceInfoProductQuery = new InterfaceInfoProduct();
        BeanUtils.copyProperties(interfaceInfoProductQueryRequest, interfaceInfoProductQuery);
        long current = interfaceInfoProductQueryRequest.getCurrent();
        long size = interfaceInfoProductQueryRequest.getPageSize();
        String sortField = interfaceInfoProductQueryRequest.getSortField();
        String sortOrder = interfaceInfoProductQueryRequest.getSortOrder();
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfoProduct> queryWrapper = new QueryWrapper<>(interfaceInfoProductQuery);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<InterfaceInfoProduct> interfaceInfoProductPage = interfaceInfoProductService.page(new Page<>(current, size), queryWrapper);

        List<InterfaceInfoProductVO> interfaceInfoProductVOList = interfaceInfoProductPage.getRecords().stream().map(interfaceInfoProduct -> {
            InterfaceInfoProductVO interfaceInfoProductVO = new InterfaceInfoProductVO();
            BeanUtils.copyProperties(interfaceInfoProduct, interfaceInfoProductVO);
            String interfaceInfoName = interfaceInfoService.getById(interfaceInfoProduct.getInterfaceInfoId()).getName();
            interfaceInfoProductVO.setInterfaceInfoName(interfaceInfoName);

            return interfaceInfoProductVO;
        }).collect(Collectors.toList());

        Page<InterfaceInfoProductVO> interfaceInfoProductVOPage = new Page<>();

        BeanUtils.copyProperties(interfaceInfoProductPage, interfaceInfoProductVOPage);
        interfaceInfoProductVOPage.setRecords(interfaceInfoProductVOList);
        return ResultUtils.success(interfaceInfoProductVOPage);
    }

    @GetMapping("/getByInterfaceId")
    public BaseResponse<List<InterfaceInfoProductVO>> getByInterfaceId(@RequestParam Long interfaceInfoId, HttpServletRequest request){

        LambdaQueryWrapper<InterfaceInfoProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InterfaceInfoProduct::getInterfaceInfoId, interfaceInfoId);

        List<InterfaceInfoProductVO> interfaceInfoProductVOList = interfaceInfoProductService.list(queryWrapper).stream().map(interfaceInfoProduct -> {
            InterfaceInfoProductVO interfaceInfoProductVO = new InterfaceInfoProductVO();
            BeanUtils.copyProperties(interfaceInfoProduct, interfaceInfoProductVO);
            String interfaceInfoName = interfaceInfoService.getById(interfaceInfoProduct.getInterfaceInfoId()).getName();
            interfaceInfoProductVO.setInterfaceInfoName(interfaceInfoName);

            return interfaceInfoProductVO;
        }).collect(Collectors.toList());

        return ResultUtils.success(interfaceInfoProductVOList);
    }

}
