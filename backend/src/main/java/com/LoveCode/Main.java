package com.LoveCode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando mi aplicación LoveCode con Spring Boot...\n");
        SpringApplication.run(Main.class, args);
    }
}