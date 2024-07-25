package com.microservice.song;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroserviceSongApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceSongApplication.class, args);
	}

}
