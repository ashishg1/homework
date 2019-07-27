package com.loyalty.homework.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.loyalty.homework.db.DBSupplier;
import com.loyalty.homework.db.DynamoDBLocalFixture;
import com.loyalty.homework.db.DynamoDBOperations;
import com.loyalty.homework.dto.Post;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
public class PostsDAOTest {

    @BeforeClass
    public static void setUpBeforeClass() {
        final AmazonDynamoDB dynamoDB = new DynamoDBLocalFixture().startDynamoDB();
        DBSupplier.setDynamoDB(dynamoDB, "testEnv");
    }

    @Before
    public void setUp() throws Exception {
        new DynamoDBOperations(new DBSupplier()).createTables(true);
    }

    @Test
    public void testSaveAndRetrieve() {
        final PostsDAO postsDAO = new PostsDAO();
        final Post post = new Post();
        post.setMessage("something");
        post.setUserName("ashish");
        postsDAO.save(post);

        final Post resultPost = postsDAO.getPost(post.getUserName(), post.getMessageId());
        assert resultPost != null;
        assertThat("Something wrong with message", resultPost.getMessage(), is(post.getMessage()));
    }

    @Test
    public void testSaveAndRetrieveAll() {
        final PostsDAO postsDAO = new PostsDAO();
        final Post post1 = new Post();
        post1.setMessage("post1");
        post1.setUserName("ashish");
        postsDAO.save(post1);

        final Post post2 = new Post();
        post2.setMessage("post2");
        post2.setUserName("ashish");
        postsDAO.save(post2);

        final List<Post> result = postsDAO.getAllPosts("ashish");
        assertThat("All messages were not retrieved", result.size(), is(2));
        assertThat("First post was not most recent", result.get(0).getMessage(), is(post2.getMessage()));
        assertThat("Second post was not the earliest", result.get(1).getMessage(), is(post1.getMessage()));
    }

    @Test
    public void testMultipleUsersAreNotMixed() {
        final PostsDAO postsDAO = new PostsDAO();
        final Post post1 = new Post();
        post1.setMessage("post1");
        post1.setUserName("ashish");
        postsDAO.save(post1);

        final Post post2 = new Post();
        post2.setMessage("post2");
        post2.setUserName("ashish1");
        postsDAO.save(post2);

        final List<Post> result = postsDAO.getAllPosts("ashish");
        assertThat("All messages were not retrieved", result.size(), is(1));
        assertThat("Only post is not ashish's", result.get(0).getUserName(), is("ashish"));
        assertThat("Only post is not ashish's", result.get(0).getMessage(), is(post1.getMessage()));
    }
}