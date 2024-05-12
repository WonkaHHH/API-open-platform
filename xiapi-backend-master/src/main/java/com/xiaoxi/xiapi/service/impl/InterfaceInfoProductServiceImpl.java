package com.xiaoxi.xiapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxi.common.common.ErrorCode;
import com.xiaoxi.common.exception.BusinessException;
import com.xiaoxi.common.model.entity.InterfaceInfoProduct;
import com.xiaoxi.xiapi.mapper.InterfaceInfoProductMapper;
import com.xiaoxi.xiapi.service.InterfaceInfoProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class InterfaceInfoProductServiceImpl extends ServiceImpl<InterfaceInfoProductMapper, InterfaceInfoProduct>
        implements InterfaceInfoProductService {

    @Override
    public void validInterfaceInfoProduct(InterfaceInfoProduct interfaceInfoProduct, boolean add) {

        if (interfaceInfoProduct == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long interfaceInfoId = interfaceInfoProduct.getInterfaceInfoId();
        Integer specification = interfaceInfoProduct.getSpecification();
        Integer price = interfaceInfoProduct.getPrice();
        Integer stock = interfaceInfoProduct.getStock();

        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(interfaceInfoId.toString(), specification.toString(), price.toString(), stock.toString())) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }

        if (specification == null || specification <= 0 || specification > 99999) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口规格设置不正确");
        }

        if (price == null || price <= 0 || price > 99999) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口价格设置不正确");
        }

        if (stock == null || stock <= 0 || stock > 99999) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口库存设置不正确");
        }
    }

    // 扣减库存
    @Override
    @Transactional
    public InterfaceInfoProduct orderProduct(Long interfaceInfoId, Long interfaceInfoProductId) {
        // 使用乐观锁，避免超卖问题
        LambdaUpdateWrapper<InterfaceInfoProduct> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(InterfaceInfoProduct::getInterfaceInfoId, interfaceInfoId);
        updateWrapper.eq(InterfaceInfoProduct::getId, interfaceInfoProductId);
        updateWrapper.setSql("stock = stock - 1");
        updateWrapper.gt(InterfaceInfoProduct::getStock, 0);

        boolean isSuccess = update(updateWrapper);
        if (!isSuccess) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "已没有库存");
        }

        return getById(interfaceInfoProductId);
    }
}




