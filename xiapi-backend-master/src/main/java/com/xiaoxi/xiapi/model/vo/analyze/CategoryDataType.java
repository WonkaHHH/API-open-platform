package com.xiaoxi.xiapi.model.vo.analyze;

import lombok.Data;

import java.util.List;

/**
 *  封装柱状图数据
 */
@Data
public class CategoryDataType {

    List<String> interfaceInfoName;

    List<Integer> interfaceInfoCountBy7;
}
