package com.loyalty.homework.dao;

import com.loyalty.homework.dto.Reply;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReplyDAO extends DynamoDAO<Reply> {

    public void save(final Reply reply) {
        super.put(reply);
    }


    public List<Reply> getAllReplies(final String messageId) {
        return super.getAll(Reply.class, "rootMessageId,messageId,message,messageDepth", new Reply(messageId), true);
    }

    public Reply getReply(final String postId, final String replyId) {
        return super.get(postId, replyId, Reply.class);
    }
}
