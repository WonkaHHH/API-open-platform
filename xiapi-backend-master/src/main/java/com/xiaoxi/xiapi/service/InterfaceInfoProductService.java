package com.xiaoxi.xiapi.service;

import com.xiaoxi.common.model.entity.InterfaceInfoProduct;
import com.baomidou.mybatisplus.extension.service.IService;


public interface InterfaceInfoProductService extends IService<InterfaceInfoProduct> {

    void validInterfaceInfoProduct(InterfaceInfoProduct interfaceInfoProduct, boolean add);

    InterfaceInfoProduct orderProduct(Long interfaceInfoId, Long interfaceInfoProductId);
}
