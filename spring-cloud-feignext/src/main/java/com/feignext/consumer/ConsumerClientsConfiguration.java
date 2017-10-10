package com.feignext.consumer;

import com.feignext.consumer.loadbalance.SpringLoadBalancerCacheFactory;
import com.netflix.client.IClient;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.AsyncLoadBalancerAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryPolicyFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by libin on 2017/9/26.
 */
@Configuration
public class ConsumerClientsConfiguration {

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;


    @Bean
    @ConditionalOnMissingBean
    public SpringEncoder springEncoder() {
        return new SpringEncoder(this.messageConverters);
    }


    @Bean
    @ConditionalOnMissingBean
    public SpringDecoder springDecoder() {
        return new SpringDecoder(this.messageConverters);
    }



    @Autowired(required = false)
    private List<SpringClientSpecification> configurations2 = new ArrayList<>();


    @Bean
    public ConsumerContext consumerContext() {
        ConsumerContext context = new ConsumerContext();
           context.setConfigurations(configurations2);
        return context;
    }
}
