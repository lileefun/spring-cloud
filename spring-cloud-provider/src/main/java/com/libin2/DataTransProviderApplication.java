package com.libin2;

import com.feignext.provider.annotation.ProviderDataTranClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@ProviderDataTranClients(basePackages = {"com.feignext.demo"})
public class DataTransProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataTransProviderApplication.class, args);
	}
}
