package com.project.leee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = "com.project.leee")

public class LeeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeeeApplication.class, args);
	}

}
