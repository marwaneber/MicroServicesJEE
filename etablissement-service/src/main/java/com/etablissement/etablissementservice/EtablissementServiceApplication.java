package com.etablissement.etablissementservice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients

public class EtablissementServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(EtablissementServiceApplication.class, args);
	}
		}
