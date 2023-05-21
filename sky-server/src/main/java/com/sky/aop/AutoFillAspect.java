package com.sky.aop;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Component
@Aspect
@Slf4j
public class AutoFillAspect {
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void pt() {
    }

    @Before("pt()")
    public void autoFill(JoinPoint point) {
        log.info("公共字段填充·······························");
        //获取切点方法的参数/实体
        Object[] args = point.getArgs();
        Object arg = args[0];
        //判断，如果实体类
        if (args == null || args.length == 0) {
            log.info("参数列表为空，所以无需进行公共字段的填充，提前结束方法");
            return;
        }
        //获取方法签名
        Signature signature = point.getSignature();
        //为什么可以强转，因为切点对象一定是方法，因为spring的切点仅支持方法级别
        //为什么需要强转？？因为MethodSignature可以获取方法的对象，而后可以通过方法对象获取方法上标注的注解
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        //获取方法上标注的注解对象
        AutoFill annotation = method.getAnnotation(AutoFill.class);
        try {
            //判断自定义注解的属性值
            if (annotation.value() == OperationType.INSERT) {
                Method createUser = args[0].getClass().getMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method createTime = args[0].getClass().getMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                createUser.invoke(arg, BaseContext.getCurrentId());
                createTime.invoke(arg, LocalDateTime.now());
            }
            Method updateUser = args[0].getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
            Method updateTime = args[0].getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            updateTime.invoke(arg, LocalDateTime.now());
            updateUser.invoke(arg, BaseContext.getCurrentId());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
