package com.yongfill.server.global.aspect;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Log4j2
@Aspect
@Component
public class LogAOP {

    private static Logger LOGGER = LoggerFactory.getLogger(Log4j2.class);
    //domain 이하 api 패키지의 모든 메서드에서 진행
    @Pointcut("execution(* com.yongfill.server..api.*.*(..))")
    private void cut(){}


    @Before("cut()")
    public void logBefore(JoinPoint joinPoint){

        //메서드 정보 받기
        Method method = getMethod(joinPoint);
        LOGGER .info("======= method {} start ========", method.getName());

        //파라미터 받아오기
        Object[] args = joinPoint.getArgs();
        if(args.length<=0) LOGGER .info("no parameter");
        for(Object arg : args){
            LOGGER .info("parameter : {}", arg);
        }
    }

    @AfterReturning(value = "cut()", returning = "returnObj")
    public void afterReturnLog(JoinPoint joinPoint, Object returnObj) {
        // 메서드 정보 받아오기
        Method method = getMethod(joinPoint);

        if (returnObj != null) {
            LOGGER .info("return type = {}", returnObj.getClass().getSimpleName());
            LOGGER .info("return value = {}", returnObj);
        }

        LOGGER .info("======= method {} ends =======", method.getName());
    }

    // JoinPoint로 메서드 정보 가져오기
    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }


}
