package com.bjca.ecopyright.util;

import java.lang.annotation.*;

/**
 * @Author by pikzas.
 * @Date 2016-11-10
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Cache {
    int expireTime() default 60;
}
