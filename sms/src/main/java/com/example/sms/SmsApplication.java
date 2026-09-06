package com.example.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmsApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(SmsApplication.class);
        String dbUrl = System.getenv("DATABASE");
        if (dbUrl != null && !dbUrl.isEmpty()) {
            app.setAdditionalProfiles("prod");
        }
        app.run(args);
		
		SpringApplication.run(SmsApplication.class, args);
	}
	
}
