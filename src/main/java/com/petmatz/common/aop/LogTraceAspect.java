package com.petmatz.common.aop;

import com.petmatz.common.config.ThreadLocalLogTrace;
import com.petmatz.common.config.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LogTraceAspect {

    private final ThreadLocalLogTrace logTrace;

    public LogTraceAspect(ThreadLocalLogTrace trace) {
        this.logTrace = trace;
    }

    @Pointcut("execution(* com.petmatz.*(..)) && " +
//            "!execution(* spring.basic.api..*Controller.*(..)) && " +
            "!execution(* com.petmatz.common.config.*.*(..)) && " +
            "!execution(* com.petmatz.common.aop.*.*(..))")
    public void allMethodsExceptControllers() {}

    @Around("allMethodsExceptControllers()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceStatus status = null;
        try {
            String shortString = joinPoint.getSignature().toShortString();
            status = logTrace.begin(shortString);

            Object result = joinPoint.proceed();

            logTrace.end(status);
            return result;
        } catch (Exception e) {
            if (status != null) {
                logTrace.exception(status, e);
            }
            throw e;
        }
    }
}