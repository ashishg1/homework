package com.loyalty.homework.dao;

import com.loyalty.homework.dto.Post;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostsDAO extends DynamoDAO<Post> {

    public void save(final Post post) {
        super.put(post);
    }

    public List<Post> getAllPosts(final String user) {
        return super.getAll(Post.class, "userName,messageId,message,city", new Post(user), false);
    }

    public Post getPost(String userName, String postId) {
        return super.get(userName, postId, Post.class);
    }
}
