package com.loyalty.homework.controllers;

import com.loyalty.homework.dto.City;
import com.loyalty.homework.dto.Message;
import com.loyalty.homework.dto.Post;
import com.loyalty.homework.dto.Reply;
import com.loyalty.homework.services.MessageService;
import com.loyalty.homework.util.CityLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Rest Api Controller
 */
@RestController
public class RestApiController {


    private final MessageService messageService;

    @Autowired
    public RestApiController(@NonNull final MessageService messageService) {
        this.messageService = messageService;
    }

    @CrossOrigin
    @RequestMapping(value = "/api/v1/users/{user}/post", method = RequestMethod.POST)
    public Post post(@PathVariable final String user, @RequestBody final String message) {
        return messageService.post(user, message);
    }

    @CrossOrigin
    @RequestMapping(value = "/api/v1/posts/{postId}/reply", method = RequestMethod.POST)
    public Reply replyToPost(@PathVariable final String postId, @RequestBody final String message, final @RequestParam Optional<String> replyUserName) {
        return messageService.replyToPost(postId, message, replyUserName);
    }

    @CrossOrigin
    @RequestMapping(value = "/api/v1/posts/{postId}/{replyId}/reply", method = RequestMethod.POST)
    public Reply replyToReply(@PathVariable final String postId, @PathVariable final String replyId, @RequestBody final String message, final @RequestParam Optional<String> replyUserName) {
        return messageService.replyToReply(postId, replyId, message, replyUserName);
    }

    @CrossOrigin
    @RequestMapping(value = "/api/v1/users/{user}/posts", method = RequestMethod.GET)
    public List<Message> getAllPosts(@PathVariable final String user) {
        return messageService.getAllPosts(user);
    }

    @CrossOrigin
    @RequestMapping(value = "/api/v1/users/{user}/posts/{postId}", method = RequestMethod.GET)
    public List<Message> getPost(@PathVariable final String user, @PathVariable final String postId) {
        return messageService.getPost(user, postId);
    }

    @CrossOrigin
    @RequestMapping(value = "/api/v1/cities", method = RequestMethod.GET)
    public Collection<String> getCities() {
        return CityLoader.getCities();
    }
}
