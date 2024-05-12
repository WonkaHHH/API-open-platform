package com.xiaoxi.xiapi.model.dto.interfaceinfoorder;

import lombok.Data;

import java.io.Serializable;

@Data
public class InterfaceInfoOrderAddRequest implements Serializable {

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
