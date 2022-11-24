package com.nut.generate_tablestructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class GenerateTableStructureApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenerateTableStructureApplication.class, args);
    }

}
