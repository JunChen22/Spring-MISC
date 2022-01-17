package com.example.spring.rabbitmq.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		System.out.println("spring rabbitmq demo start");
		SpringApplication.run(DemoApplication.class, args);
	}
}
