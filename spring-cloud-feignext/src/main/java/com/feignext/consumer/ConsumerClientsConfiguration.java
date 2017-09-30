package com.feignext.consumer;

import com.feignext.consumer.loadbalance.SpringLoadBalancerCacheFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryPolicyFactory;
import org.springframework.cloud.netflix.ribbon.RibbonClientSpecification;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancedRetryPolicyFactory;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by libin on 2017/9/26.
 */
@Configuration
public class ConsumerClientsConfiguration {

    // ribbon 配置
    @Autowired(required = false)
    private List<RibbonClientSpecification> configurations = new ArrayList<>();

    @Bean
    @ConditionalOnMissingBean
    public SpringClientFactory springClientFactory() {
        SpringClientFactory factory = new SpringClientFactory();
        factory.setConfigurations(this.configurations);
        return factory;
    }

    @Bean
    @Primary
    @ConditionalOnMissingClass("org.springframework.retry.support.RetryTemplate")
    public SpringLoadBalancerCacheFactory springLoadBalancerCacheFactory(
            SpringClientFactory factory) {
        return new SpringLoadBalancerCacheFactory(factory);
    }


    @Bean
    @ConditionalOnClass(name = "org.springframework.retry.support.RetryTemplate")
    @ConditionalOnMissingBean
    public LoadBalancedRetryPolicyFactory loadBalancedRetryPolicyFactory(SpringClientFactory clientFactory) {
        //设置普通的负载均衡
        return new RibbonLoadBalancedRetryPolicyFactory(clientFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public Client consumerClient(SpringLoadBalancerCacheFactory loadBalancerCacheFactory,
                              SpringClientFactory clientFactory) {
        return new LoadBalancerClient(new Client.Default(null, null),
                loadBalancerCacheFactory, clientFactory);
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
