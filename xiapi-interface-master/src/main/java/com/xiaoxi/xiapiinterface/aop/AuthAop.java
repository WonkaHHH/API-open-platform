package com.xiaoxi.xiapiinterface.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class AuthAop {

    /**
     *  鉴定请求是否来自 gateway
     * @param joinPoint
     * @return
     * @throws Throwable
     */

    @Around("execution(* com.xiaoxi.xiapiinterface.controller.*.*(..))")
    public Object AuthSource(ProceedingJoinPoint joinPoint) throws Throwable {

        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        if (!"xiaoxi_api_gateway".equals(request.getHeader("api_source"))){
            return null;
        }

        return joinPoint.proceed();
    }
}
