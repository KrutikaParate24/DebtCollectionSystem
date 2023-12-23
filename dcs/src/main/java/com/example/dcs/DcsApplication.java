package com.example.dcs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DcsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DcsApplication.class, args);
	}

}
