package com.feignext.consumer.loadbalance;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryPolicyFactory;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancedRetryPolicyFactory;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.Map;

/**
 * 全局负载均衡工厂类
 * Created by libin on 2017/9/29.
 */
public class SpringLoadBalancerCacheFactory {

    private final SpringClientFactory factory;
    private final LoadBalancedRetryPolicyFactory loadBalancedRetryPolicyFactory;
    private boolean enableRetry = false;

    private volatile Map<String, ClientLoadBalancer> cache = new ConcurrentReferenceHashMap<>();

    public SpringLoadBalancerCacheFactory(SpringClientFactory factory) {
        this.factory = factory;
        this.loadBalancedRetryPolicyFactory = new RibbonLoadBalancedRetryPolicyFactory(factory);
    }

    public SpringLoadBalancerCacheFactory(SpringClientFactory factory,
                                            LoadBalancedRetryPolicyFactory loadBalancedRetryPolicyFactory) {
        this.factory = factory;
        this.loadBalancedRetryPolicyFactory = loadBalancedRetryPolicyFactory;
    }

    public SpringLoadBalancerCacheFactory(SpringClientFactory factory,
                                            LoadBalancedRetryPolicyFactory loadBalancedRetryPolicyFactory, boolean enableRetry) {
        this.factory = factory;
        this.loadBalancedRetryPolicyFactory = loadBalancedRetryPolicyFactory;
        this.enableRetry = enableRetry;
    }

    /**
     * 创建获取负载均衡
     * @param clientName
     * @return
     */
    public ClientLoadBalancer create(String clientName) {
        if (this.cache.containsKey(clientName)) {
            return this.cache.get(clientName);
        }
        IClientConfig config = this.factory.getClientConfig(clientName);
        ILoadBalancer lb = this.factory.getLoadBalancer(clientName);
        ServerIntrospector serverIntrospector = this.factory.getInstance(clientName, ServerIntrospector.class);
        ClientLoadBalancer client = new ClientLoadBalancer(lb, config, serverIntrospector);
        this.cache.put(clientName, client);
        return client;
    }
}
