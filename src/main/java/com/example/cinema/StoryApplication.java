package com.example.cinema;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class StoryApplication {

    public static void main(String[] args) {

//        SpringApplication.run(CinemaApplication.class, args);
        SpringApplicationBuilder builder = new SpringApplicationBuilder(StoryApplication.class);
        builder.headless(false).run(args);
    }

}
