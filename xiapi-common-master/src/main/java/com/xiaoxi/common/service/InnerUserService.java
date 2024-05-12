package com.xiaoxi.common.service;

import com.xiaoxi.common.model.entity.User;

public interface InnerUserService {

    /**
     *  查询该ak sk是否分配给用户
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);
}
