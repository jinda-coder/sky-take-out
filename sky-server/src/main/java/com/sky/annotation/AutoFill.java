package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AutoFill {
    OperationType value() default OperationType.INSERT;
}
