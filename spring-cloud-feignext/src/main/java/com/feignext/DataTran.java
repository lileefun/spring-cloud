package com.feignext;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by libin on 2017/9/7.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(DataTranRegistrar.class)
public @interface DataTran {
}
