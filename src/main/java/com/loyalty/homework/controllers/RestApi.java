package com.loyalty.homework.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApi {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "Hello world1";
    }
}
