package com.xiaoxi.xiapi.model.dto.interfaceinfoorder;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class InterfaceInfoOrderUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 订单状态，1：未支付；2：已支付；3：已取消；4：退款中；6：已退款
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}
