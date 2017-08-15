package com.config;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Configuration;

@Configuration
@RibbonClient(name = "appname-libin", configuration = TestConfiguration.class)//注册再eureka上的appname
public class TestConfiguration {

}