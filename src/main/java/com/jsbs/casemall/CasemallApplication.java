package com.jsbs.casemall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CasemallApplication {

	public static void main(String[] args) {
		SpringApplication.run(CasemallApplication.class, args);
	}

}
