package com.amcrest.unity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AmcrestApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmcrestApplication.class, args);
    }

}
