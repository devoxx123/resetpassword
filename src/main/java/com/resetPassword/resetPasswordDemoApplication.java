package com.resetPassword;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({ "com.resetPassword" })
@EntityScan({ "com.resetPassword"})
@EnableJpaRepositories({ "com.resetPassword"})
public class resetPasswordApplication {

	public static void main(String[] args) {
		SpringApplication.run(resetPasswordApplication .class, args);
	}
}
