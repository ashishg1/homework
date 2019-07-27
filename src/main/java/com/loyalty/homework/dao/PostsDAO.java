package com.loyalty.homework.dao;

import com.loyalty.homework.dto.Post;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DAO for Posts
 */
@Repository
public class PostsDAO extends DynamoDAO<Post> {

    /**
     * Saves a post
     *
     * @param post The post to be saved
     */
    public void save(@NonNull final Post post) {
        super.put(post);
    }

    /**
     * Get all posts for a user
     *
     * @param user the username
     * @return List of posts for the user
     */
    @NonNull
    public List<Post> getAllPosts(@NonNull final String user) {
        return super.getAll(Post.class, "userName,messageId,message,city", new Post(user), false);
    }

    /**
     * Get a single post for a user and postId
     *
     * @param userName the user
     * @param postId   post id
     * @return The matching post
     */
    @Nullable
    public Post getPost(@NonNull final String userName, @NonNull final String postId) {
        return super.get(userName, postId, Post.class);
    }
}
