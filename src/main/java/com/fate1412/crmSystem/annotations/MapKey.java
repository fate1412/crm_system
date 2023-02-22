package com.fate1412.crmSystem.annotations;

import java.lang.annotation.*;

/**
 * 为MyCollections.toMap定义key值，默认时则用属性名作为key值
 * 只有使用注解的属性才会被加入map
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MapKey {
    String value() default "";
    
    boolean nonNull() default true;
}
