package com.xiaoxi.xiapi.model.dto.interfaceinfoorder;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xiaoxi.common.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class InterfaceInfoOrderQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
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


    private static final long serialVersionUID = 1L;
}
