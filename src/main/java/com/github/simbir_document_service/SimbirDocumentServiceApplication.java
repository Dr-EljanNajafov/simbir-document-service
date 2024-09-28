package com.github.simbir_document_service;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.github.simbir_document_service.client")
public class SimbirDocumentServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SimbirDocumentServiceApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }
}
