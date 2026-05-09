package com.learnova.lesson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class LessonServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LessonServiceApplication.class, args);
    }
}