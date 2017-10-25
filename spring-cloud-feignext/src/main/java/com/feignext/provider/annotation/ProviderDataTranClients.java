package com.feignext.provider.annotation;

import com.feignext.provider.DataTranProviderRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by libin on 2017/9/7.
 * 提供者者注册
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(DataTranProviderRegistrar.class)
public @interface ProviderDataTranClients {

    String[] value() default {};

    String[] basePackages() default {};

}
