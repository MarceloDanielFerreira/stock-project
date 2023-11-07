package com.app.stockproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableCaching
/*@Configuration
@ComponentScans({
        @ComponentScan(basePackages = {"main.java.com.app.stockproject.config"}),
        @ComponentScan(basePackages = {"main.java.com.app.stockproject.controller"}),
        @ComponentScan(basePackages = {"main.java.com.app.stockproject.dao"}),
        @ComponentScan(basePackages = {"main.java.com.app.stockproject.dto"}),
        @ComponentScan(basePackages = {"main.java.com.app.stockproject.service"}),
        @ComponentScan(basePackages = {"main.java.com.app.stockproject.bean"}),
        @ComponentScan(basePackages = {"main.java.com.app.stockproject.utils"})

})*/

public class StockProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockProjectApplication.class, args);
    }

}
