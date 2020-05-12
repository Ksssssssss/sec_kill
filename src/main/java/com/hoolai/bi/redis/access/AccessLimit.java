package com.hoolai.bi.redis.access;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimit {
    String actionId();
    String second();
    String maxCount();
    String capacity();
    String quota() default "1";
}
