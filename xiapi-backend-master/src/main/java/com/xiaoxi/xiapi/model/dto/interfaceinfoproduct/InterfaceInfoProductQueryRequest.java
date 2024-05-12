package com.xiaoxi.xiapi.model.dto.interfaceinfoproduct;

import com.xiaoxi.common.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

@Data
public class InterfaceInfoProductQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 接口 id
     */
    private Long interfaceInfoId;

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
