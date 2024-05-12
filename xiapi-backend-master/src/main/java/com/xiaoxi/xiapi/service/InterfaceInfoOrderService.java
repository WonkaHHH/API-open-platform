package com.xiaoxi.xiapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoxi.common.model.entity.InterfaceInfoOrder;
import com.xiaoxi.xiapi.model.dto.interfaceinfoorder.InterfaceInfoOrderAddRequest;
import com.xiaoxi.xiapi.model.dto.interfaceinfoorder.InterfaceInfoOrderPayRequest;

import javax.servlet.http.HttpServletRequest;


public interface InterfaceInfoOrderService extends IService<InterfaceInfoOrder> {

    void validInterfaceInfoOrder(InterfaceInfoOrder interfaceInfoOrder, boolean add);

    long addOrder(InterfaceInfoOrderAddRequest interfaceInfoOrderAddRequest,  Long userId);

    Long payOrder(InterfaceInfoOrderPayRequest interfaceInfoOrderPayRequest, Long userId);
}
