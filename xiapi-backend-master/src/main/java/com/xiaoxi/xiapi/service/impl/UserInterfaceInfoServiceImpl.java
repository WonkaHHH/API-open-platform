package com.xiaoxi.xiapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxi.common.common.ErrorCode;
import com.xiaoxi.common.exception.BusinessException;
import com.xiaoxi.common.model.entity.UserInterfaceInfo;
import com.xiaoxi.xiapi.service.UserInterfaceInfoService;
import com.xiaoxi.xiapi.mapper.UserInterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
        implements UserInterfaceInfoService {

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long userId = userInterfaceInfo.getUserId();
        Long interfaceInfoId = userInterfaceInfo.getInterfaceInfoId();
        Integer totalNum = userInterfaceInfo.getTotalNum();
        Integer leftNum = userInterfaceInfo.getLeftNum();
        Integer status = userInterfaceInfo.getStatus();

        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(userId.toString(), interfaceInfoId.toString(), totalNum.toString(), leftNum.toString(), status.toString())) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }

        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户id不正确");
        }
        if (interfaceInfoId == null || interfaceInfoId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口id不正确");
        }
        if (totalNum == null || totalNum <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "总次数数值不正确");
        }
        if (leftNum == null || leftNum <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余次数数值不正确");
        }
        if (status == null || status > 1 || status < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "状态设置参数错误");
        }
    }

    // TODO 后续考虑事务问题，加锁   leftNum >= 0
    @Override
    public boolean invokeCount(long interfaceId, long userId) {

        if (interfaceId <= 0 || userId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        LambdaUpdateWrapper<UserInterfaceInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserInterfaceInfo::getInterfaceInfoId, interfaceId);
        updateWrapper.eq(UserInterfaceInfo::getUserId, userId);
        updateWrapper.setSql("totalNum = totalNum + 1, leftNum = leftNum - 1");

        return this.update(updateWrapper);
    }

    // 获取剩余调用次数
    @Override
    public int getLeftNum(long interfaceId, long userId) {

        LambdaQueryWrapper<UserInterfaceInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInterfaceInfo::getInterfaceInfoId, interfaceId);
        queryWrapper.eq(UserInterfaceInfo::getUserId, userId);

        UserInterfaceInfo userInterfaceInfo = getOne(queryWrapper);
        if (userInterfaceInfo == null){
            return 0;
        }
        return userInterfaceInfo.getLeftNum();
    }

    /**
     *  支付成功后添加调用次数
     * @param userId
     * @param interfaceInfoId
     * @param specification
     * @return
     */
    @Override
    public boolean addInvokeCount(Long userId, Long interfaceInfoId, Integer specification) {
        if (userId == null || userId <= 0 || interfaceInfoId == null || interfaceInfoId <= 0 || specification == null || specification < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "添加调用次数参数错误");
        }

        // 判断是否是第一次，如果是第一次则创建插入记录
        LambdaQueryWrapper<UserInterfaceInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInterfaceInfo::getUserId, userId);
        queryWrapper.eq(UserInterfaceInfo::getInterfaceInfoId, interfaceInfoId);
        UserInterfaceInfo userInterfaceInfo = getOne(queryWrapper);
        if (userInterfaceInfo == null){
            UserInterfaceInfo userInterfaceInfo1 = new UserInterfaceInfo();
            userInterfaceInfo1.setUserId(userId);
            userInterfaceInfo1.setInterfaceInfoId(interfaceInfoId);
            userInterfaceInfo1.setTotalNum(specification);
            userInterfaceInfo1.setLeftNum(specification);
            userInterfaceInfo1.setStatus(0);
            save(userInterfaceInfo1);
        }

        LambdaUpdateWrapper<UserInterfaceInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserInterfaceInfo::getUserId, userId);
        updateWrapper.eq(UserInterfaceInfo::getInterfaceInfoId, interfaceInfoId);
        updateWrapper.setSql("leftNum = leftNum +" + specification);

        return update(updateWrapper);
    }


}




