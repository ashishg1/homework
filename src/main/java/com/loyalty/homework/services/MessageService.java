package com.loyalty.homework.services;

import com.google.common.collect.Lists;
import com.loyalty.homework.dao.PostsDAO;
import com.loyalty.homework.dao.ReplyDAO;
import com.loyalty.homework.dto.Message;
import com.loyalty.homework.dto.Post;
import com.loyalty.homework.dto.Reply;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Message service handles all actions associated with handling messages
 */
@SuppressWarnings("WeakerAccess")
@Service
public class MessageService {


    private final PostsDAO postsDAO;

    private final ReplyDAO replyDAO;

    @Autowired
    public MessageService(final PostsDAO postsDAO, final ReplyDAO replyDAO) {
        this.postsDAO = postsDAO;
        this.replyDAO = replyDAO;
    }


    /**
     * Service call that creates a root posts and persists it.
     *
     * @param userName The username of the user
     * @param post     the post to be saved
     * @return unique message id associated with the message
     */
    @NonNull
    public Post post(@NonNull final String userName, @NonNull final Post post) {
        Validate.notEmpty(userName, "User Name can not be empty");
        final Post postToSave = new Post();
        postToSave.setUserName(userName);
        postToSave.setMessage(post.getMessage());
        postToSave.setCity(post.getCity());
        postsDAO.save(postToSave);
        return postToSave;
    }

    /**
     * Service call that creates a reply to a post
     *
     * @param postId the post that is being replied to
     * @param reply  the reply
     * @return the new unique id associated with the reply
     */
    @NonNull
    public Reply replyToPost(@NonNull final String postId, @NonNull final Reply reply) {
        Validate.notEmpty(postId, "Post id of post can not be empty");
        final Reply replyToSave = new Reply();
        replyToSave.setRootMessageId(postId);
        replyToSave.setMessage(reply.getMessage());
        replyToSave.setMessageDepth(1);
        replyToSave.setCity(reply.getCity());
        replyToSave.setUserName(reply.getUserName() == null || reply.getUserName().trim().isEmpty() ? "anoymous" : reply.getUserName());
        replyDAO.save(replyToSave);
        return replyToSave;

    }

    /**
     * Service all that creates a reply that is to another reply
     *
     * @param postId  the root post being replied to
     * @param replyId the reply that is being replied to
     * @param reply   the reply
     * @return the unique id associated with the reply
     */
    @NonNull
    public Reply replyToReply(@NonNull final String postId, @NonNull final String replyId, @NonNull final Reply reply) {
        Validate.notEmpty(postId, "PostId id of post can not be empty");
        Validate.notEmpty(replyId, "Reply id of parent reply can not be empty");
        final Reply parentReply = replyDAO.getReply(postId, replyId);
        if (parentReply == null) {
            throw new IllegalStateException("Something very wrong the parent reply does not exist: " + replyId);
        }
        final Reply replyToSave = new Reply();
        replyToSave.setRootMessageId(postId);
        replyToSave.setMessage(reply.getMessage());
        replyToSave.setMessageDepth(parentReply.getMessageDepth() + 1);
        replyToSave.setMessageId(parentReply.getMessageId() + "_" + replyToSave.getMessageId());
        replyToSave.setCity(reply.getCity());
        replyToSave.setUserName(reply.getUserName() == null || reply.getUserName().trim().isEmpty() ? "anoymous" : reply.getUserName());
        replyDAO.save(replyToSave);
        return replyToSave;
    }

    /**
     * Get a post in thread format
     *
     * @param userName the userName belonging to poster
     * @param postId   postId to retrieve
     * @return a list of messages representing post and its replies
     */
    @NonNull
    public List<Message> getPost(@NonNull final String userName, @NonNull final String postId) {
        Validate.notEmpty(postId, "PostId id of post can not be empty");
        Validate.notEmpty(userName, "userName can not be empty");
        final List<Message> messages = Lists.newArrayList();
        final Post post = postsDAO.getPost(userName, postId);
        if (post == null) {
            return messages;
        }
        appendPostToThread(messages, post);
        return messages;
    }

    /**
     * Returns all the posts including all replies for a user
     *
     * @param user the user to retrieve posts for
     * @return list of messages represeting all posts
     */
    @NonNull
    public List<Message> getAllPosts(@NonNull final String user) {
        Validate.notEmpty(user, "user name can not be empty");
        final List<Message> messages = Lists.newArrayList();
        final List<Post> posts = postsDAO.getAllPosts(user);
        posts.forEach(post -> appendPostToThread(messages, post));
        return messages;
    }

    private void appendPostToThread(final List<Message> messages, final Post post) {
        messages.add(post);
        final List<Reply> allReplies = replyDAO.getAllReplies(post.getMessageId());
        messages.addAll(allReplies);

    }

    /**
     * This service call returns the next posts that are available for a user
     *
     * @param user   The user name
     * @param postId the post id to start from
     * @param count  the number of posts to return
     * @return list of posts based on the query
     */
    @SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "unused"})
    @NonNull
    public List<Post> getNextPost(@NonNull final String user, final Optional<String> postId, final Optional<Integer> count) {
        throw new UnsupportedOperationException("This operation has yet to be built");
    }
}
