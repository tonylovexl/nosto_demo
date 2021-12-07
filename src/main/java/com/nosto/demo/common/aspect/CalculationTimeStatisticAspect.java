package com.nosto.demo.common.aspect;

import com.nosto.demo.vo.BaseResponseEntity;
import com.nosto.demo.vo.ErrorResponseEntity;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class CalculationTimeStatisticAspect {

    private static final ThreadLocal<Long> START_TIME = new ThreadLocal<>();
    private static final ThreadLocal<String> REQUEST_PATH = new ThreadLocal<>();

    @Pointcut("@annotation(com.nosto.demo.common.aspect.CalculationTimeStatistic)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        START_TIME.set(System.currentTimeMillis());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String path = request.getContextPath() + request.getServletPath();
        REQUEST_PATH.set(path);

        log.info("request API:" + path);
    }

    @AfterReturning(returning = "res", pointcut = "pointcut()")
    public void doAfterReturning(Object res) {
        BaseResponseEntity responseEntity = ((ResponseEntity<BaseResponseEntity>) res).getBody();
        if (responseEntity.isSuccess()) {
            responseEntity.setCostTime(System.currentTimeMillis() - START_TIME.get() + "(ms)");
        } else {
            ErrorResponseEntity errorResponseEntity = (ErrorResponseEntity) responseEntity;
            errorResponseEntity.setPath(REQUEST_PATH.get());
        }

        START_TIME.remove();
        REQUEST_PATH.remove();

        log.debug("processing time:" + responseEntity.getCostTime());
    }
}
