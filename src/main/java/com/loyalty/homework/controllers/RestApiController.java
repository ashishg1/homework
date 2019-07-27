package com.loyalty.homework.controllers;

import com.loyalty.homework.dto.Message;
import com.loyalty.homework.dto.Post;
import com.loyalty.homework.dto.Reply;
import com.loyalty.homework.services.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Rest Api Controller that will handle all the API's for the Homework project
 * Ideally each resource retrieval should be separated into multiple controllers but here since the
 * project is kind of small we will keep it simple to make life simpler
 */
@RestController
@Api(value = "/", produces = "application/json")
@RequestMapping("/api/v1")
public class RestApiController {

    private final MessageService messageService;

    /**
     * Rest API Controller constructor
     *
     * @param messageService message service that will take care of all the posts and replies
     */
    @Autowired
    public RestApiController(@NonNull final MessageService messageService) {
        this.messageService = messageService;
    }

    @ApiOperation(value = "Create a new post for user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User's Post including City details"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @CrossOrigin
    @RequestMapping(value = "/users/{user}/post", method = RequestMethod.POST)
    public Post post(@PathVariable final String user, @RequestBody final Post post) {
        return messageService.post(user, post);
    }

    @ApiOperation(value = "Create a new reply to a post")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Created reply including City details"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @CrossOrigin
    @RequestMapping(value = "/posts/{postId}/reply", method = RequestMethod.POST)
    public Reply replyToPost(@PathVariable final String postId, @RequestBody final Reply reply) {
        return messageService.replyToPost(postId, reply);
    }

    @ApiOperation(value = "Create a new reply to another reply")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Created Reply including City details"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @CrossOrigin
    @RequestMapping(value = "/posts/{postId}/{replyId}/reply", method = RequestMethod.POST)
    public Reply replyToReply(@PathVariable final String postId, @PathVariable final String replyId, @RequestBody final Reply reply) {
        return messageService.replyToReply(postId, replyId, reply);
    }

    @ApiOperation(value = "Get all posts for a user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of posts and their replies for a user"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @CrossOrigin
    @RequestMapping(value = "/users/{user}/posts", method = RequestMethod.GET)
    public List<Message> getAllPosts(@PathVariable final String user) {
        return messageService.getAllPosts(user);
    }


    @ApiOperation(value = "Get single post for a user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Post and its replies for a user"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @CrossOrigin
    @RequestMapping(value = "/users/{user}/posts/{postId}", method = RequestMethod.GET)
    public List<Message> getPost(@PathVariable final String user, @PathVariable final String postId) {
        return messageService.getPost(user, postId);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @ApiOperation(value = "Get next available posts for a user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get next (count) posts for a user starting from optionally provided lastPostId"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @CrossOrigin
    @RequestMapping(value = "/users/{user}/post/next", method = RequestMethod.GET)
    public List<Post> getNextPost(@PathVariable final String user, @RequestParam final Optional<String> lastPostId, @RequestParam final Optional<Integer> count) {
        return messageService.getNextPost(user, lastPostId, count);
    }
}
