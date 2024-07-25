package com.microservice.playlist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class MicroservicePlaylistApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicePlaylistApplication.class, args);
	}

}
