package com.xiaoxi.common.service;

import com.xiaoxi.common.model.entity.InterfaceInfo;

public interface InnerInterfaceInfoService {

    /**
     * 从数据库中查询模拟接口是否存在
     * @param url
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String url, String method);
}
