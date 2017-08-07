package com.yjfei.paas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.extern.slf4j.Slf4j;

@EnableDiscoveryClient
@SpringBootApplication
@EnableTransactionManagement  
@Slf4j
public class PaaSApplication {

	public static void main(String[] args) {	
		SpringApplication.run(PaaSApplication.class, args);			
		log.info("paas scheduler start success!");
	}
}
