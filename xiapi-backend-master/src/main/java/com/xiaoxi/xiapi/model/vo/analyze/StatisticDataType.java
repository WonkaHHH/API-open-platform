package com.xiaoxi.xiapi.model.vo.analyze;

import lombok.Data;

/**
 *  封装数据分析数据
 */
@Data
public class StatisticDataType {

    private Integer shopInterfaceCount;

    private Integer invokeInterfaceTotalCount;

    private Integer invokeInterfaceLeftCount;

    private String invokeMoreInterface;
}
