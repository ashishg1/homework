package com.loyalty.homework.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.loyalty.homework.db.DBSupplier;
import com.loyalty.homework.db.DynamoDBLocalFixture;
import com.loyalty.homework.db.DynamoDBOperations;
import com.loyalty.homework.dto.Reply;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
public class ReplyDAOTest {

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
        final ReplyDAO replyDAO = new ReplyDAO();
        final Reply reply = new Reply();
        reply.setRootMessageId("post1");
        reply.setMessage("something");
        replyDAO.save(reply);

        final Reply resultReply = replyDAO.getReply(reply.getRootMessageId(), reply.getMessageId());
        assert resultReply != null;
        assertThat("Something wrong with message", resultReply.getMessage(), is(reply.getMessage()));
    }

    @Test
    public void testSaveAndRetrieveAll() {
        final ReplyDAO replyDAO = new ReplyDAO();
        final Reply reply1 = new Reply();
        reply1.setRootMessageId("post1");
        reply1.setMessage("reply1");
        replyDAO.save(reply1);

        final Reply reply2 = new Reply();
        reply2.setRootMessageId("post1");
        reply2.setMessage("reply2");
        replyDAO.save(reply2);

        final List<Reply> result = replyDAO.getAllReplies("post1");
        assertThat("All messages were not retrieved", result.size(), is(2));
        assertThat("First post was not the earliest", result.get(0).getMessage(), is(reply1.getMessage()));
        assertThat("Second post was not the latest", result.get(1).getMessage(), is(reply2.getMessage()));
    }

    @Test
    public void testMultiplePostsAreNotMixed() {
        final ReplyDAO replyDAO = new ReplyDAO();
        final Reply reply1 = new Reply();
        reply1.setRootMessageId("post1");
        reply1.setMessage("reply1");
        replyDAO.save(reply1);

        final Reply reply2 = new Reply();
        reply2.setRootMessageId("post2");
        reply2.setMessage("reply2");
        replyDAO.save(reply2);


        final List<Reply> result = replyDAO.getAllReplies("post1");
        assertThat("All messages were not retrieved", result.size(), is(1));
        assertThat("Only post is not from post1", result.get(0).getRootMessageId(), is("post1"));
    }
}