package com.example.servicedeliberationordinaire;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
public class ServiceDeliberationOrdinaireApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceDeliberationOrdinaireApplication.class, args);
    }

}
