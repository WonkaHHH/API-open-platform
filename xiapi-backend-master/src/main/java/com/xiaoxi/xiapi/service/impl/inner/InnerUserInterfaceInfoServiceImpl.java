package com.xiaoxi.xiapi.service.impl.inner;

import com.xiaoxi.common.service.InnerUserInterfaceInfoService;
import com.xiaoxi.xiapi.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public boolean invokeCount(long interfaceId, long userId) {
        return userInterfaceInfoService.invokeCount(interfaceId, userId);
    }

    @Override
    public int getLeftNum(long interfaceId, long userId) {

        return userInterfaceInfoService.getLeftNum(interfaceId, userId);
    }
}
