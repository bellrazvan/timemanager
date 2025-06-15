package com.time.timemanager;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.util.Objects;

@EnableCaching
@SpringBootApplication
public class TimemanagerApplication {
	public static void main(String[] args) {
		System.setProperty("java.net.preferIPv4Stack", "true");
		System.setProperty("java.net.preferIPv4Addresses", "true");

		final Dotenv dotenv = Dotenv.load();
		System.setProperty("JWT_SECRET", Objects.requireNonNull(dotenv.get("JWT_SECRET")));
		System.setProperty("SPRING_MAIL_USERNAME", Objects.requireNonNull(dotenv.get("SPRING_MAIL_USERNAME")));
		System.setProperty("SPRING_MAIL_PASSWORD", Objects.requireNonNull(dotenv.get("SPRING_MAIL_PASSWORD")));

		System.setProperty("DB_URL", Objects.requireNonNull(dotenv.get("DB_URL")));
		System.setProperty("DB_USERNAME", Objects.requireNonNull(dotenv.get("DB_USERNAME")));
		System.setProperty("DB_PASSWORD", Objects.requireNonNull(dotenv.get("DB_PASSWORD")));

		SpringApplication.run(TimemanagerApplication.class, args);
	}
}