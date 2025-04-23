package org.project1.shopweb.component.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Aspect
@Component
public class PerformanceAspec {

    private Logger logger = Logger.getLogger(getClass().getName());

    private String getMethod(JoinPoint joinPoint){
        return joinPoint.getSignature().getName();
    }

    @Pointcut("within(org.project1.shopweb.controller.CategoryController)")
    //  @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerPointCut(){}

    @Before("controllerPointCut()")
    public void loggBefore(JoinPoint joinPoint){
        logger.info(getMethod(joinPoint) + "is executing");
    }

    @After("controllerPointCut()")
    public void loggAfter(JoinPoint joinPoint){
        logger.info(getMethod(joinPoint) + "is executing");
    }

    @Around("controllerPointCut()")
    public Object loggAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.nanoTime();

        Object returnValue = proceedingJoinPoint.proceed(); // Gọi method gốc

        long end = System.nanoTime();

        String methodName = proceedingJoinPoint.getSignature().getName();
        logger.info("method " + methodName + " executed in "
                + TimeUnit.NANOSECONDS.toMillis(end - start) + " ms");

        return returnValue; // ✅ Trả lại response cho controller
    }




}
