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
    public Post post(@NonNull final String userName, @NonNull final Post post) {
        Validate.notEmpty(userName, "User Name can not be null or empty");
        Validate.notNull(post, "Post can not be null or empty");
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
    public Reply replyToPost(@NonNull final String postId, @NonNull final Reply reply) {
        Validate.notEmpty(postId, "Post id of post can not be null or empty");
        Validate.notNull(reply, "Reply can not be null or empty");
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
    public Reply replyToReply(@NonNull final String postId, @NonNull final String replyId, @NonNull final Reply reply) {
        Validate.notEmpty(replyId, "Reply id of parent reply can not be null or empty");
        Validate.notNull(reply, "Reply can not be null or empty");
        final Reply parentReply = replyDAO.getReply(postId, replyId);
        if (parentReply == null) {
            throw new IllegalStateException("Something very wrong the parent reply does not exist: " + replyId);
        }
        final Reply replyToSave = new Reply();
        replyToSave.setRootMessageId(postId);
        replyToSave.setMessage(reply.getMessage());
        replyToSave.setMessageDepth(parentReply.getMessageDepth() + 1);
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
    public List<Message> getPost(@NonNull final String userName, @NonNull final String postId) {
        final List<Message> messages = Lists.newArrayList();
        final Post post = postsDAO.getPost(userName, postId);
        appendPostToThread(messages, post);
        return messages;
    }

    /**
     * Returns all the posts including all replies for a user
     *
     * @param user the user to retrieve posts for
     * @return list of messages represeting all posts
     */
    public List<Message> getAllPosts(@NonNull final String user) {
        final List<Message> messages = Lists.newArrayList();
        final List<Post> posts = postsDAO.getAllPosts(user);
        if (posts != null) {
            posts.forEach(post -> appendPostToThread(messages, post));
        }
        return messages;
    }

    private void appendPostToThread(final List<Message> messages, final Post post) {
        messages.add(post);
        final List<Reply> allReplies = replyDAO.getAllReplies(post.getMessageId());
        if (allReplies != null) {
            messages.addAll(allReplies);
        }
    }

    public Post getNextPost(final String user, final Optional<String> postId) {
        throw new UnsupportedOperationException("This operation has yet to be built");
    }
}
