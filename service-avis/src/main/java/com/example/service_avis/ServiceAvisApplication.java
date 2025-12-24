package com.example.service_avis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ServiceAvisApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceAvisApplication.class, args);
	}

}
