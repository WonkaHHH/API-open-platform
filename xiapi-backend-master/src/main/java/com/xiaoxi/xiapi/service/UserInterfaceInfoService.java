package com.xiaoxi.xiapi.service;

import com.xiaoxi.common.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;


public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validUserInterfaceInfo(UserInterfaceInfo interfaceinfo, boolean add);

    boolean invokeCount(long interfaceId, long userId);

    int getLeftNum(long interfaceId, long userId);

    boolean addInvokeCount(Long userId, Long interfaceInfoId, Integer specification);
}
