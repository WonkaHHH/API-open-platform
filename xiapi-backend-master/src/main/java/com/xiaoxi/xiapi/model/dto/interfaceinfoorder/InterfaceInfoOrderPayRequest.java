package com.xiaoxi.xiapi.model.dto.interfaceinfoorder;

import lombok.Data;

import java.io.Serializable;

@Data
public class InterfaceInfoOrderPayRequest implements Serializable {

    /**
     * 订单id
     */
    private Long id;

    /**
     * 接口id
     */
    private Long interfaceInfoId;

    /**
     * 商品id
     */
    private Long interfaceInfoProductId;


    private static final long serialVersionUID = 1L;
}
