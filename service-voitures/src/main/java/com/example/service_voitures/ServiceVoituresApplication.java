package com.example.service_voitures;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ServiceVoituresApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceVoituresApplication.class, args);
	}

}
