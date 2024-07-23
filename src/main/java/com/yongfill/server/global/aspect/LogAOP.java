package com.yongfill.server.global.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Slf4j
@Aspect
public class LogAOP {

    //domain 이하 api 패키지의 모든 메서드에서 진행
    @Pointcut("execution(* com.yongfill.server..api.*.*(..))")
    private void cut(){}


    @Before("cut()")
    public void logBefore(JoinPoint joinPoint){

        //메서드 정보 받기
        Method method = getMethod(joinPoint);
        log.info("======= method {} start ========", method.getName());

        //파라미터 받아오기
        Object[] args = joinPoint.getArgs();
        if(args.length<=0) log.info("no parameter");
        for(Object arg : args){
            log.info("parameter : {}", arg);
        }
    }

    @AfterReturning(value = "cut()", returning = "returnObj")
    public void afterReturnLog(JoinPoint joinPoint, Object returnObj) {
        // 메서드 정보 받아오기
        Method method = getMethod(joinPoint);

        if (returnObj != null) {
            log.info("return type = {}", returnObj.getClass().getSimpleName());
            log.info("return value = {}", returnObj);
        }

        log.info("======= method {} ends =======", method.getName());
    }

    // JoinPoint로 메서드 정보 가져오기
    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }


}
