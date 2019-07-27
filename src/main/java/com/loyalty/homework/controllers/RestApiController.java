package com.loyalty.homework.controllers;

import com.loyalty.homework.dto.Message;
import com.loyalty.homework.dto.Post;
import com.loyalty.homework.dto.Reply;
import com.loyalty.homework.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

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
    public Post post(@PathVariable final String user, @RequestBody final Post post) {
        return messageService.post(user, post);
    }

    @CrossOrigin
    @RequestMapping(value = "/api/v1/posts/{postId}/reply", method = RequestMethod.POST)
    public Reply replyToPost(@PathVariable final String postId, @RequestBody final Reply reply) {
        return messageService.replyToPost(postId, reply);
    }

    @CrossOrigin
    @RequestMapping(value = "/api/v1/posts/{postId}/{replyId}/reply", method = RequestMethod.POST)
    public Reply replyToReply(@PathVariable final String postId, @PathVariable final String replyId, @RequestBody final Reply reply) {
        return messageService.replyToReply(postId, replyId, reply);
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
    @RequestMapping(value = "/api/v1/users/{user}/post/next", method = RequestMethod.GET)
    public Post getNextPost(@PathVariable final String user, @RequestParam final Optional<String> postId) {
        return messageService.getNextPost(user, postId);
    }
}
