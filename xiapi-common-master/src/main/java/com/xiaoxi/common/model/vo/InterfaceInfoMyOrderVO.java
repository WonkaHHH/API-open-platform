package com.xiaoxi.common.model.vo;

import lombok.Data;
import java.util.Date;

@Data
public class InterfaceInfoMyOrderVO {
    /**
     * id
     */
    private Long id;

    /**
     * 接口id
     */
    private Long interfaceInfoId;

    /**
     *  接口名称
     */
    private String interfaceInfoName;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 商品id
     */
    private Long interfaceInfoProductId;

    /**
     * 规格
     */
    private Integer specification;

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
}
