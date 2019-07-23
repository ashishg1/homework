package com.loyalty.homework;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class SpringBootApp {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "Hello world";
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootApp.class, args);
    }
}