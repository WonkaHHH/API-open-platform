package com.xiaoxi.common.model.vo;

import lombok.Data;

@Data
public class UserInterfaceInfoVO {

    private Long id;

    /**
     * 调用用户 id
     */
    private Long userId;

    private String interfaceInfoName;

    /**
     * 接口 id
     */
    private Long interfaceInfoId;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;
}
