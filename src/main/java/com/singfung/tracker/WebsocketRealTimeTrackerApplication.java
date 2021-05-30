package com.singfung.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WebsocketRealTimeTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebsocketRealTimeTrackerApplication.class, args);
    }

}
