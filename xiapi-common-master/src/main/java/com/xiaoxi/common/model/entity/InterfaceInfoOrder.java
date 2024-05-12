package com.xiaoxi.common.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 接口商品订单
 * @TableName interface_info_order
 */
@TableName(value ="interface_info_order")
@Data
public class InterfaceInfoOrder implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 接口id
     */
    private Long interfaceInfoId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 商品id
     */
    private Long interfaceInfoProductId;

    /**
     * 价格
     */
    private Integer price;

    /**
     * 订单状态，1：未支付；2：已支付；3：已取消；4：退款中；6：已退款
     */
    private Integer status;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 退款时间
     */
    private Date refundTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}