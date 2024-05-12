package com.xiaoxi.xiapi.mapper;

import com.xiaoxi.common.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    Long getInvokeMoreInterfaceByTotalNum(@Param("userId") Long userId);
}




