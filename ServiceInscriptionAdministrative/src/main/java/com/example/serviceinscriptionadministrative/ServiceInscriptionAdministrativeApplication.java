package com.example.serviceinscriptionadministrative;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ServiceInscriptionAdministrativeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceInscriptionAdministrativeApplication.class, args);

    }

}
