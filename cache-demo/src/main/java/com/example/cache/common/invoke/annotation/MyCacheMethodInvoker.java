package com.example.cache.common.invoke.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface MyCacheMethodInvoker {
    String value() default "";
}
