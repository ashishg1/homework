package com.loyalty.homework.controllers;

import com.loyalty.homework.dto.Message;
import com.loyalty.homework.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Rest Api Controller
 */
@RestController
public class RestApiController {

    @Autowired
    private MessageService messageService;

    /**
     * This api returns the text that is passed to it
     *
     * @param text source text
     * @return same as source text
     */
    @CrossOrigin
    @RequestMapping(value = "/api/v1/clone", method = RequestMethod.POST)
    public Message clone(@RequestBody final String text) {
        return messageService.saveMessage(text);
    }
}
