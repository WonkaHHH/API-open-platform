package com.xiaoxi.xiapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoxi.common.model.entity.InterfaceInfo;
import com.xiaoxi.common.model.entity.UserInterfaceInfo;
import com.xiaoxi.xiapi.mapper.InterfaceInfoMapper;
import com.xiaoxi.xiapi.mapper.UserInterfaceInfoMapper;
import com.xiaoxi.xiapi.model.vo.analyze.CategoryDataType;
import com.xiaoxi.xiapi.model.vo.analyze.PieDataType;
import com.xiaoxi.xiapi.model.vo.analyze.StatisticDataType;
import com.xiaoxi.xiapi.service.AnalyzeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalyzeServiceImpl implements AnalyzeService {

    @Resource
    InterfaceInfoMapper interfaceInfoMapper;

    @Resource
    UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Override
    @Transactional
    public List<PieDataType> getPerInterfaceCount(long userId) {

        LambdaQueryWrapper<UserInterfaceInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInterfaceInfo::getUserId, userId);
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoMapper.selectList(queryWrapper);

        return userInterfaceInfoList.stream().map(userInterfaceInfo -> {
            PieDataType pieDataType = new PieDataType();
            String interfaceInfoName = interfaceInfoMapper.selectOne(new LambdaQueryWrapper<InterfaceInfo>().eq(InterfaceInfo::getId, userInterfaceInfo.getInterfaceInfoId())).getName();
            Integer totalNum = userInterfaceInfoMapper.selectOne(new LambdaQueryWrapper<UserInterfaceInfo>().eq(UserInterfaceInfo::getInterfaceInfoId, userInterfaceInfo.getInterfaceInfoId()).eq(UserInterfaceInfo::getUserId, userInterfaceInfo.getUserId())).getTotalNum();

            pieDataType.setName(interfaceInfoName);
            pieDataType.setValue(totalNum);

            return pieDataType;
        }).collect(Collectors.toList());
    }

    @Override
    public List<PieDataType> getPerInterfaceLeftCount(Long userId) {
        LambdaQueryWrapper<UserInterfaceInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInterfaceInfo::getUserId, userId);
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoMapper.selectList(queryWrapper);

        return userInterfaceInfoList.stream().map(userInterfaceInfo -> {
            PieDataType pieDataType = new PieDataType();
            String interfaceInfoName = interfaceInfoMapper.selectOne(new LambdaQueryWrapper<InterfaceInfo>().eq(InterfaceInfo::getId, userInterfaceInfo.getInterfaceInfoId())).getName();
            Integer totalNum = userInterfaceInfoMapper.selectOne(new LambdaQueryWrapper<UserInterfaceInfo>().eq(UserInterfaceInfo::getInterfaceInfoId, userInterfaceInfo.getInterfaceInfoId()).eq(UserInterfaceInfo::getUserId, userInterfaceInfo.getUserId())).getLeftNum();

            pieDataType.setName(interfaceInfoName);
            pieDataType.setValue(totalNum);

            return pieDataType;
        }).collect(Collectors.toList());
    }

    @Override
    public CategoryDataType getInterfaceInvokeCountFor7(Long userId) {

        LambdaQueryWrapper<UserInterfaceInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInterfaceInfo::getUserId, userId);
        queryWrapper.orderByDesc(UserInterfaceInfo::getUpdateTime);
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoMapper.selectList(queryWrapper).stream().limit(7).collect(Collectors.toList());

        List<String> interfaceNameList = new ArrayList<>();
        List<Integer> interfaceInvokeList = new ArrayList<>();

        userInterfaceInfoList.forEach(userInterfaceInfo -> {
            String interfaceInfoName = interfaceInfoMapper.selectOne(new LambdaQueryWrapper<InterfaceInfo>().eq(InterfaceInfo::getId, userInterfaceInfo.getInterfaceInfoId())).getName();

            interfaceNameList.add(interfaceInfoName);
            interfaceInvokeList.add(userInterfaceInfo.getTotalNum());
        });

        CategoryDataType categoryDataType = new CategoryDataType();
        categoryDataType.setInterfaceInfoName(interfaceNameList);
        categoryDataType.setInterfaceInfoCountBy7(interfaceInvokeList);

        return categoryDataType;
    }

    @Override
    public StatisticDataType getStatistic(Long userId) {

        Long shopInterfaceCount = userInterfaceInfoMapper.selectCount(new LambdaQueryWrapper<UserInterfaceInfo>().eq(UserInterfaceInfo::getUserId, userId));

        Integer invokeInterfaceTotalCount = 0;
        Integer invokeInterfaceLeftCount = 0;
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoMapper.selectList(new LambdaQueryWrapper<UserInterfaceInfo>().eq(UserInterfaceInfo::getUserId, userId));
        for (UserInterfaceInfo userInterfaceInfo : userInterfaceInfoList) {
            invokeInterfaceTotalCount += userInterfaceInfo.getTotalNum();
            invokeInterfaceLeftCount += userInterfaceInfo.getLeftNum();
        }

        Long invokeMoreInterfaceId = userInterfaceInfoMapper.getInvokeMoreInterfaceByTotalNum(userId);
        String invokeMoreInterface = interfaceInfoMapper.selectOne(new LambdaQueryWrapper<InterfaceInfo>().eq(InterfaceInfo::getId, invokeMoreInterfaceId)).getName();

        StatisticDataType result = new StatisticDataType();
        result.setShopInterfaceCount(shopInterfaceCount.intValue());
        result.setInvokeInterfaceTotalCount(invokeInterfaceTotalCount);
        result.setInvokeInterfaceLeftCount(invokeInterfaceLeftCount);
        result.setInvokeMoreInterface(invokeMoreInterface);

        return result;
    }
}
