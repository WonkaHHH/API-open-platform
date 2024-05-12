package com.xiaoxi.common.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class InterfaceInfoProductVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 接口 id
     */
    private Long interfaceInfoId;

    /**
     * 接口 名称
     */
    private String interfaceInfoName;

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
