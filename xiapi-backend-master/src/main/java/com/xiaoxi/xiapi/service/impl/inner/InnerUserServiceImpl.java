package com.xiaoxi.xiapi.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoxi.common.common.ErrorCode;
import com.xiaoxi.common.exception.BusinessException;
import com.xiaoxi.common.model.entity.User;
import com.xiaoxi.common.service.InnerUserService;
import com.xiaoxi.xiapi.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getInvokeUser(String accessKey) {
        if (StringUtils.isAnyBlank(accessKey)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getAccessKey, accessKey);

        return userMapper.selectOne(queryWrapper);
    }
}
