package com.example.filiereservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FiliereServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FiliereServiceApplication.class, args);
    }

}
