package com.feignext.consumer;

import org.springframework.cloud.context.named.NamedContextFactory;

/**
 * Created by libin on 2017/9/26.
 */
public class ConsumerContext extends NamedContextFactory {

    public ConsumerContext() {
        super(ConsumerClientsConfiguration.class, "consumer", "ext.client.name");
    }


}
