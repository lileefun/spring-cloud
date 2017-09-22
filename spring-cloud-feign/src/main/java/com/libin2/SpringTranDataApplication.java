package com.libin2;

import com.feignext.consumer.annotation.ConsumerDataTranClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ConsumerDataTranClients(basePackages = {"com.feignext.demo"})
public class SpringTranDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringTranDataApplication.class, args);
	}
}
