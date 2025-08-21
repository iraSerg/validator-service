package com.example.validatorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ValidatorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ValidatorServiceApplication.class, args);
    }

}
