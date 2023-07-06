package com.auto.anno;

import java.lang.annotation.*;

/**
 * redis缓存注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisCatch {
    String keyPrefix() default "";
}
