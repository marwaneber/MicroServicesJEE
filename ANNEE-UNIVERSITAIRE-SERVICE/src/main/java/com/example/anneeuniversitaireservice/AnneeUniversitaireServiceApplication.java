package com.example.anneeuniversitaireservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AnneeUniversitaireServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnneeUniversitaireServiceApplication.class, args);
    }

}
