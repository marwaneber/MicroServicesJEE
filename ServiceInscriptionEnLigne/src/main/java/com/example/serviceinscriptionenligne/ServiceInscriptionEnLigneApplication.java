package com.example.serviceinscriptionenligne;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
public class ServiceInscriptionEnLigneApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServiceInscriptionEnLigneApplication.class, args);
	}
	}