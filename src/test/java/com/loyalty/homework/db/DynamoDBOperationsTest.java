package com.loyalty.homework.db;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.loyalty.homework.dao.PostsDAO;
import com.loyalty.homework.dao.ReplyDAO;
import com.loyalty.homework.dto.Post;
import com.loyalty.homework.dto.Reply;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
public class DynamoDBOperationsTest {

    @BeforeClass
    public static void setUpBeforeClass() {
        final AmazonDynamoDB dynamoDB = new DynamoDBLocalFixture().startDynamoDB();
        DBSupplier.setDynamoDB(dynamoDB, "testEnv");
    }

    @Test
    public void createTablesWithoutDelete() throws Exception {
        new DynamoDBOperations(new DBSupplier()).createTables(true);
        new PostsDAO().save(new Post("ashish"));
        new ReplyDAO().save(new Reply("ashish"));
        new DynamoDBOperations(new DBSupplier()).createTables(false);
        assertThat("Post got deleted", new PostsDAO().getAllPosts("ashish").get(0).getUserName(), is("ashish"));
        assertThat("Reply got deleted", new ReplyDAO().getAllReplies("ashish").get(0).getRootMessageId(), is("ashish"));
    }

    @Test
    public void createTablesWithDelete() throws Exception {
        new DynamoDBOperations(new DBSupplier()).createTables(true);
        new PostsDAO().save(new Post("ashish"));
        new ReplyDAO().save(new Reply("ashish"));
        new DynamoDBOperations(new DBSupplier()).createTables(true);
        assertThat("Post got deleted", new PostsDAO().getAllPosts("ashish").size(), is(0));
        assertThat("Reply got deleted", new ReplyDAO().getAllReplies("ashish").size(), is(0));
    }
}