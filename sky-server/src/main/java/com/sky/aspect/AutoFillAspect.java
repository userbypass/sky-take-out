package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Component
@Aspect
@Slf4j
public class AutoFillAspect {
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {
    }

    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("公共字段自动填充执行");
        // 获取接口方法的方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取接口的方法
        Method method = signature.getMethod();
        // 获取方法上的指定注解
        AutoFill anno = method.getAnnotation(AutoFill.class);
        // 获取操作类型，Update/Insert
        OperationType operationType = anno.value();
        // 获取方法的参数
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            log.info("待填充参数不存在，自动填充失败");
            return;
        }
        // 此处的实体一般是DTO对象
        Object entity = args[0];
        switch (operationType) {
            // 插入方法需要填充创建时间、创建人、更新时间和更新人四条信息
            case INSERT -> {
                log.info("插入操作字段填充");
                try {
                    // 通过反射获取相关方法
                    Class<?> entityClass = entity.getClass();
                    Method setCreateTime = entityClass.getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                    Method setCreateUser = entityClass.getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                    Method setUpdateTime = entityClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                    Method setUpdateUser = entityClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                    // 执行这些方法
                    setCreateTime.invoke(entity, LocalDateTime.now());
                    setCreateUser.invoke(entity, BaseContext.getCurrentId());
                    setUpdateTime.invoke(entity, LocalDateTime.now());
                    setUpdateUser.invoke(entity, BaseContext.getCurrentId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 更新方法需要填充更新时间和更新人四条信息
            case UPDATE -> {
                log.info("更新操作字段填充");
                try {
                    Class<?> entityClass = entity.getClass();
                    Method setUpdateTime = entityClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                    Method setUpdateUser = entityClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                    setUpdateTime.invoke(entity, LocalDateTime.now());
                    setUpdateUser.invoke(entity, BaseContext.getCurrentId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            default -> {
                log.info("非法操作类型，自动填充失败");
            }
        }
    }
}
