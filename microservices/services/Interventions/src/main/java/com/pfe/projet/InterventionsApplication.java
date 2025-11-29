package com.pfe.projet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class InterventionsApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterventionsApplication.class, args);
    }

}
