package com.feignext.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.context.named.NamedContextFactory;

/**
 * Created by libin on 2017/9/29.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
class SpringClientSpecification implements NamedContextFactory.Specification {

    private String name;

    private Class<?>[] configuration;

}