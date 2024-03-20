package com.found_404.funco;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class FuncoApplication {

	public static void main(String[] args) {
		SpringApplication.run(FuncoApplication.class, args);
	}

}
