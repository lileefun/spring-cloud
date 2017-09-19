package com.libin2;

import com.feignext.DataTranClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@DataTranClients(basePackages = {"com.feignext.demo"})
@EnableFeignClients
public class SpringTranDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringTranDataApplication.class, args);
	}
}
