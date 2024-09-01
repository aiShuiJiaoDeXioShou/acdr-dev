package com.yskj.acdr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AcdrApplication {
    public static void main(String[] args) {
        SpringApplication.run(AcdrApplication.class, args);
    }
}
