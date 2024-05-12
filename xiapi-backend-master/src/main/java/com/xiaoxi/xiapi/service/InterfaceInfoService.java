package com.xiaoxi.xiapi.service;

import com.xiaoxi.common.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;


public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceinfo, boolean add);

    Object invokeInterface(String accessKey, String secretKey, long id, String userRequestParams);
}
