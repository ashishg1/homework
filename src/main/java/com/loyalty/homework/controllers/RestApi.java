package com.loyalty.homework.controllers;

import com.loyalty.homework.dto.Message;
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
    public Message clone(@RequestBody final String text) {
        return new Message(text);
    }
}
