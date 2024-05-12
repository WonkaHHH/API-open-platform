package com.xiaoxi.xiapi.aop;
import com.xiaoxi.common.common.ErrorCode;
import com.xiaoxi.common.exception.BusinessException;
import com.xiaoxi.common.model.entity.User;
import com.xiaoxi.common.model.enums.UserRoleEnum;
import com.xiaoxi.xiapi.annotation.AuthCheck;
import com.xiaoxi.xiapi.annotation.MustLogin;
import com.xiaoxi.xiapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 *  拦截检查是否登录
 */
@Aspect
@Component
@Slf4j
public class CheckLoginInterceptor {

    @Resource
    private UserService userService;

    @Around("@annotation(mustLogin)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, MustLogin mustLogin) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 获取登录用户
        User loginUser = userService.getLoginUser(request);
        // 检查是否登录
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 通过权限校验，放行
        return joinPoint.proceed();
    }
}
