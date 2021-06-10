package com.adesso.digitalwash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableAutoConfiguration
@EnableEurekaServer
public class ServiceDiscoveryStarter {
	
	public static void main(String...args){
		System.setProperty("spring.devtools.restart.enabled", "false");
		SpringApplication.run(ServiceDiscoveryStarter.class, args);
	}
}
