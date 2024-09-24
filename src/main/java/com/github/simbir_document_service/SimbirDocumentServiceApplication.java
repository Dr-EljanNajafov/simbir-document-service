package com.github.simbir_document_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SimbirDocumentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SimbirDocumentServiceApplication.class, args);
    }
}
