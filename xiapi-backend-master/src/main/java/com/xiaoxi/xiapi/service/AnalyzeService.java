package com.xiaoxi.xiapi.service;

import com.xiaoxi.xiapi.model.vo.analyze.CategoryDataType;
import com.xiaoxi.xiapi.model.vo.analyze.PieDataType;
import com.xiaoxi.xiapi.model.vo.analyze.StatisticDataType;

import java.util.List;

public interface AnalyzeService {
    List<PieDataType> getPerInterfaceCount(long userId);

    List<PieDataType> getPerInterfaceLeftCount(Long userId);

    CategoryDataType getInterfaceInvokeCountFor7(Long userId);

    StatisticDataType getStatistic(Long userId);
}
