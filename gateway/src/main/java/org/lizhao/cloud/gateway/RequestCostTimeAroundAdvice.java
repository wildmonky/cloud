package org.lizhao.cloud.gateway;

import org.aspectj.lang.ProceedingJoinPoint;
import org.lizhao.base.model.ResponseBodyModel;

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
@Deprecated
public class RequestCostTimeAroundAdvice{

//    @Pointcut(" within(org.lizhao.cloud.gateway.business.controller..*) ")
    public void allControllerMethod() {}

//    @Around("allControllerMethod()")
    public Object requestTimeCostCalc(ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }

//    @Around("allControllerMethod()")
    public Object responseResultAfterAdvice(ProceedingJoinPoint joinPoint) {
        Object result;
        try {
            result = joinPoint.proceed();
            return ResponseBodyModel.success(result);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return ResponseBodyModel.error(throwable.getMessage());
        }
    }

}
