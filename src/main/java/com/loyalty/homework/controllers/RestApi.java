package com.loyalty.homework.controllers;

import org.springframework.web.bind.annotation.*;

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
    @CrossOrigin
    @RequestMapping(value = "/api/v1/clone", method = RequestMethod.POST)
    public String clone(@RequestBody final String text) {
        return text;
    }
}
