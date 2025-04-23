package org.project1.shopweb.component.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.logging.Logger;

@Component
@Aspect
public class UserActivityLogger {
    Logger logger = Logger.getLogger(getClass().getName());
    @Pointcut("within(org.project1.shopweb.controller.UserController)")
    public void UserLogger(){}

    //execution([kiểu trả về] [tên class].[tên method](tham số))

    @Around("UserLogger()")

    public Object LoggerUserActivity(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String methodName = proceedingJoinPoint.getSignature().getName();
        String remoteAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getRemoteAddr();

        logger.info("method: " + methodName + " start at IP address: " + remoteAddress);

        Object result = proceedingJoinPoint.proceed(); // Gọi method gốc và giữ lại kết quả

        logger.info(methodName + " finished");

        return result; // ✅ Trả lại kết quả cho client
    }


}
