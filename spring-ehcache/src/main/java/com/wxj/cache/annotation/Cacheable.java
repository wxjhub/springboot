package com.wxj.cache.annotation;

import java.lang.annotation.*;

/**
 * @author wangxinji
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Cacheable {
    String cacheName();

    String key() default "";
}
