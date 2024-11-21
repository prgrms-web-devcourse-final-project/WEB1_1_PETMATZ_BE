package com.petmatz.common.aop;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger("AOP_LOGGER");

//    @Pointcut("execution(* spring.basic.api..*Controller.*(..))")
    @Pointcut("execution(* com.petmatz.api..*Controller.*(..))")
    public void controllerMethods() {
    }

    @Around("controllerMethods()")
    public Object requestControllerUserInfoSendToELK(ProceedingJoinPoint joinPoint) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // RequestContext가 없는 경우 처리
        if (requestAttributes == null) {
            return joinPoint.proceed(); // 원래 메서드 실행
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        Map<String, Object> logMap = new HashMap<>();
        putUserInfo(request, logMap);
        return logRequest(joinPoint, response, logMap);
    }

    private static void putUserInfo(HttpServletRequest request, Map<String, Object> logMap) {
        logMap.put("Request IP", request.getRemoteAddr());
        logMap.put("Request URI", request.getRequestURI());
        logMap.put("HTTP Method", request.getMethod());
        logMap.put("Request Params", request.getQueryString());
        logMap.put("User-Agent", request.getHeader("User-Agent"));
        logMap.put("Referer", request.getHeader("Referer"));
    }

    private static Object logRequest(ProceedingJoinPoint joinPoint, HttpServletResponse response, Map<String, Object> logMap) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;

            logMap.put("Response Status", response.getStatus());
            logMap.put("Request Duration (ms)", duration);
            logger.info(logMap.toString());
            return result;
        } catch (Exception ex) {
            long duration = System.currentTimeMillis() - startTime;

            int statusCode = response.getStatus() == 0 ? 500 : response.getStatus();
            logMap.put("Response Status", statusCode);
            logMap.put("Request Duration (ms)", duration);
            logMap.put("Error Message", ex.getMessage());
            logMap.put("Exception", ex.getClass().getSimpleName());
            logger.error(logMap.toString(), ex);
            throw ex;
        }
    }
}