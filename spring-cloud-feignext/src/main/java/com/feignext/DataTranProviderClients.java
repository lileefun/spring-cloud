package com.feignext;

import com.feignext.provider.DataTranProviderRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by libin on 2017/9/7.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(DataTranProviderRegistrar.class)
public @interface DataTranProviderClients {

    String[] value() default {};

    String[] basePackages() default {};

}
