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

@Slf4j
@Aspect
@Component
public class LogAOP {

    //domain 이하 패키지의 모든 메서드에서 진행
    @Pointcut("execution(* com.yongfill.server.domain..*.*(..))")
    private void cut(){}


    @Before("cut()")
    public void logBefore(JoinPoint joinPoint){

        //메서드 정보 받기
        Method method = getMethod(joinPoint);
        log.info("============== method name : {} ==============");

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
        log.info("======= method name = {} =======", method.getName());

        log.info("return type = {}", returnObj.getClass().getSimpleName());
        log.info("return value = {}", returnObj);
    }

    // JoinPoint로 메서드 정보 가져오기
    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }


}
