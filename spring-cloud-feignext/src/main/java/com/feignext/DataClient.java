package com.feignext;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Created by libin on 2017/9/7.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface  DataClient {

    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";


    String ref();

    /**
     * spring bean id
     * @return
     */
    String beanid();

    Class<?>[] configuration() default {};

}
