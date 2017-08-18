package com.libin.test.api.annotation;

import java.lang.annotation.*;

/**
 * Created by libin on 2016/10/20.
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiParam {

    String value() default "";

    boolean required() default false;

    public boolean jsonType() default false;

    public String defaultValue() default "";

    public String validateRegx() default  "";
}
