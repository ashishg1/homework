package com.loyalty.homework.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Api Controller
 */
@RestController
public class RestApi {

    /**
     * This api returns the text that is passed to it
     *
     * @param text source text
     * @return same as source text
     */
    @RequestMapping(value = "/api/v1/clone", method = RequestMethod.GET)
    public String clone(@RequestParam(value = "text", required = false) final String text) {
        return text;
    }
}
