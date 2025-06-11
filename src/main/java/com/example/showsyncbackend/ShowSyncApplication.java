package com.example.showsyncbackend;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ShowSyncApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShowSyncApplication.class, args);
    }


}
