package com.example.serviceDelibrationSemestre;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;



@SpringBootApplication
@EnableFeignClients
public class ServiceDelibrationSemestreApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceDelibrationSemestreApplication.class, args);
	}


}
