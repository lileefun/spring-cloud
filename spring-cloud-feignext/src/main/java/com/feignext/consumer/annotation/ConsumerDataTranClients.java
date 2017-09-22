package com.feignext.consumer.annotation;

import com.feignext.consumer.DataTranConsumerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by libin on 2017/9/7.
 * 消费者注册
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(DataTranConsumerRegistrar.class)
public @interface ConsumerDataTranClients {

    String[] value() default {};

    String[] basePackages() default {};

}
