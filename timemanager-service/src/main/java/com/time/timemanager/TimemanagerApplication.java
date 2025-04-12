package com.time.timemanager;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Objects;

@SpringBootApplication
public class TimemanagerApplication {
	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.setProperty("JWT_SECRET", Objects.requireNonNull(dotenv.get("JWT_SECRET")));
		System.setProperty("SPRING_MAIL_USERNAME", Objects.requireNonNull(dotenv.get("SPRING_MAIL_USERNAME")));
		System.setProperty("SPRING_MAIL_PASSWORD", Objects.requireNonNull(dotenv.get("SPRING_MAIL_PASSWORD")));

		SpringApplication.run(TimemanagerApplication.class, args);
	}
}