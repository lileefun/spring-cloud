package com.libin.test.beanconf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by libin on 2017/8/21.
 */
@Component
@ConfigurationProperties(prefix = "redis")
@PropertySource(value  = {"classpath:redis-application.yml"},encoding="utf-8")
public class RedisConfiguration {

    private static Logger logger = LoggerFactory.getLogger(RedisConfiguration.class);
    @Value("${ips}")
    private String ips;

    @Value("${maxredirections}")
    private int maxredirections;

    @Value("${timeout}")
    private int timeout;


    @Value("${maxTotal}")
    private int maxTotal;

    @Value("${maxIdle}")
    private int maxIdle;


    @Value("${minIdle}")
    private int minIdle;


    @Value("${maxWait}")
    private int maxWait;

    @Value("${testOnBorrow}")
    private Boolean testOnBorrow;


    @Value("${testWhileIdle}")
    private Boolean testWhileIdle;
    @Bean
    public JedisPoolConfig getRedisConfig(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setMaxWaitMillis(maxWait);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestWhileIdle(testWhileIdle);
        return config;
    }

    @Bean
    public JedisCluster getJedisCluster(){
        Set<HostAndPort> list = new LinkedHashSet<>();
        JedisPoolConfig config = getRedisConfig();
        String[] split = ips.split(",");
        for (String ip : split) {
            String[] split1 = ip.split(":");
            HostAndPort hostAndPort = new HostAndPort(split1[0],Integer.parseInt(split1[1]));
            list.add(hostAndPort);
        }
        JedisCluster jedisCluster = new JedisCluster(list,timeout,maxredirections,config);
        logger.info("init JedisCluster ...");
        return jedisCluster;
    }

}
