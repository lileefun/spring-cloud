package com.libin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
public class SpringCloudProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudProviderApplication.class, args);
	}
}
