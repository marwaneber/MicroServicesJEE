package com.example.inscription_pedagogique;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class  InscriptionPedagogiqueApplication {

    public static void main(String[] args) {
        SpringApplication.run(InscriptionPedagogiqueApplication.class, args);
    }


}
