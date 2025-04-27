package com.example.sistema_cla;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.example.sistema_cla")
public class SistemaClaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaClaApplication.class, args);
    }

}
