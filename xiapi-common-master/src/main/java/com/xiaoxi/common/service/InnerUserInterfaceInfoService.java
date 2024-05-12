package com.xiaoxi.common.service;


public interface InnerUserInterfaceInfoService{

    /**
     *  调用次数 +1
     * @param interfaceId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceId, long userId);

    /**
     *  获取剩余调用次数
     * @param interfaceId
     * @param userId
     * @return
     */
    int getLeftNum(long interfaceId, long userId);
}
