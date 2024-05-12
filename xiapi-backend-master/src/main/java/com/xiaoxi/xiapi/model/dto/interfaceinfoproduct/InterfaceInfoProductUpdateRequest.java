package com.xiaoxi.xiapi.model.dto.interfaceinfoproduct;

import lombok.Data;

import java.io.Serializable;

@Data
public class InterfaceInfoProductUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 规格
     */
    private Integer specification;

    /**
     * 价格
     */
    private Integer price;

    /**
     * 库存
     */
    private Integer stock;


    private static final long serialVersionUID = 1L;
}
