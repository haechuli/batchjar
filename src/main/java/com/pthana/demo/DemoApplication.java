package com.pthana.demo;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		int exitCode = 0;

		try {
			exitCode = SpringApplication.exit(SpringApplication.run(DemoApplication.class, args));
		} catch (Exception e) {
			exitCode = 5;
		} finally {
			System.exit(exitCode);
		}
	}


}
