package com.example.serviceDeliberationAnnuelle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ServiceDeliberationAnnuelleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceDeliberationAnnuelleApplication.class, args);
	}

}
