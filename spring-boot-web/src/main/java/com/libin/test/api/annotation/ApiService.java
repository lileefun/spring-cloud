package com.libin.test.api.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by libin on 2016/10/20.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ApiService {

    public String value();

}
