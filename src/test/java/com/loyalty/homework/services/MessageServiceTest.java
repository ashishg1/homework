package com.loyalty.homework.services;

import com.google.common.collect.ImmutableList;
import com.loyalty.homework.dao.PostsDAO;
import com.loyalty.homework.dao.ReplyDAO;
import com.loyalty.homework.dto.Message;
import com.loyalty.homework.dto.Post;
import com.loyalty.homework.dto.Reply;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
public class MessageServiceTest {

    @MockBean
    private PostsDAO postsDAO;

    @MockBean
    private ReplyDAO replyDAO;

    private MessageService messageService;

    @Before
    public void setup() {
        messageService = new MessageService(postsDAO, replyDAO);

        final Reply reply = new Reply();
        reply.setRootMessageId("ashishPost");
        reply.setMessage("reply message");
        reply.setMessageId("ashishReply");
        reply.setMessageDepth(1);
        Mockito.when(replyDAO.getReply("ashishPost", "ashishReply")).thenReturn(reply);
        Mockito.when(replyDAO.getAllReplies("ashishPost")).thenReturn(ImmutableList.of(reply));

        final Post post = new Post();
        post.setUserName("ashish");
        post.setMessageId("ashishPost");
        post.setMessage("post message");
        Mockito.when(postsDAO.getPost("ashish", "ashishPost")).thenReturn(post);
        Mockito.when(postsDAO.getAllPosts("ashish")).thenReturn(ImmutableList.of(post));

    }

    @Test
    public void testPostReturnsValidPost() {
        final Post post = messageService.post("ashish", "new message");
        assertThat("userName is not valid", post.getUserName(), is("ashish"));
        assertThat("message is not valid", post.getMessage(), is("new message"));
    }

    @Test
    public void testReplyReturnsValidReply() {
        final Reply reply = messageService.replyToPost("ashish", "new message");
        assertThat("root id is wrong", reply.getRootMessageId(), is("ashish"));
        assertThat("message is not valid", reply.getMessage(), is("new message"));
        assertThat("message is not valid", reply.getMessageDepth(), is(1));
    }

    @Test
    public void testReplyToReplyReturnsValidReply() {
        final Reply reply = messageService.replyToReply("ashishPost", "ashishReply", "new message");
        assertThat("root id is wrong", reply.getRootMessageId(), is("ashishPost"));
        assertThat("message is not valid", reply.getMessage(), is("new message"));
        assertThat("message is not valid", reply.getMessageDepth(), is(2));
    }

    @Test(expected = IllegalStateException.class)
    public void testReplyToReplyThrowsExceptionWithInvalidPost() {
        messageService.replyToReply("ashisPost", "ashishReply", "new message");
    }

    @Test(expected = IllegalStateException.class)
    public void testReplyToReplyThrowsExceptionWithInvalidReply() {
        messageService.replyToReply("ashisPost", "ashishRply", "new message");
    }

    @Test
    public void testGetPost() {
        final List<Message> thread = messageService.getPost("ashish", "ashishPost");
        assertThat("post not returned with reply", thread.size(), is(2));
    }

    @Test
    public void testGetAllPosts() {
        final List<Message> thread = messageService.getAllPosts("ashish");
        assertThat("post not returned with reply", thread.size(), is(2));
    }


}