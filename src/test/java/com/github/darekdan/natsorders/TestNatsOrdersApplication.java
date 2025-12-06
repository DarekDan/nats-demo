package com.github.darekdan.natsorders;

import org.springframework.boot.SpringApplication;

public class TestNatsOrdersApplication {

    public static void main(String[] args) {
        SpringApplication
                .from(Application::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }

}
