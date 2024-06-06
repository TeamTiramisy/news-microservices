package com.alibou.newsmicroservices;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableFeignClients
@SpringBootApplication
public class NewServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewServiceApplication.class, args);
	}

}
