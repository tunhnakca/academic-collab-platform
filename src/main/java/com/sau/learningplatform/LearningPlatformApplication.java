package com.sau.learningplatform;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LearningPlatformApplication {

    public static void main(String[] args) {
        // get .env file
        Dotenv dotenv = Dotenv.load();

        // set properities
        System.setProperty("MAIL_USERNAME", dotenv.get("MAIL_USERNAME"));
        System.setProperty("MAIL_PASSWORD", dotenv.get("MAIL_PASSWORD"));

        SpringApplication.run(LearningPlatformApplication.class, args);
    }

}
