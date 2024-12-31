package com.petmatz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PetmetzApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetmetzApplication.class, args);
	}

}
