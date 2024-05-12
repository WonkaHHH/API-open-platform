package com.xiaoxi.xiapi.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoxi.common.common.ErrorCode;
import com.xiaoxi.common.exception.BusinessException;
import com.xiaoxi.common.model.entity.InterfaceInfo;
import com.xiaoxi.common.service.InnerInterfaceInfoService;
import com.xiaoxi.xiapi.mapper.InterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {
            if (StringUtils.isAnyBlank(url, method)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        LambdaQueryWrapper<InterfaceInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InterfaceInfo::getUrl, url);
        queryWrapper.eq(InterfaceInfo::getMethod, method);

        return interfaceInfoMapper.selectOne(queryWrapper);
    }
}
