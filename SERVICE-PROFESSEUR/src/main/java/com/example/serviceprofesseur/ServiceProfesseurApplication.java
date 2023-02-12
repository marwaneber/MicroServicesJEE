package com.example.serviceprofesseur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ServiceProfesseurApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceProfesseurApplication.class, args);
    }

}
