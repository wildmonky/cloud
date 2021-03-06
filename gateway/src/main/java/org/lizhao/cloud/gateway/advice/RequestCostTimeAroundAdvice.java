package org.lizhao.cloud.gateway.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;


/**
 * Description
 * 请求耗时计算
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-06-11 20:43
 * @since 0.0.1-SNAPSHOT
 */
//@Aspect
public class RequestCostTimeAroundAdvice{

//    @Pointcut(" within(org.lizhao.cloud.gateway.controller..*) ")
    public void allControllerMethod() {}

//    @Around("allControllerMethod()")
    public Object requestTimeCostCalc(ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }

}
