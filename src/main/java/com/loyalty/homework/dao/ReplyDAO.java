package com.loyalty.homework.dao;

import com.loyalty.homework.dto.Reply;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The Reply DAO
 */
@Repository
public class ReplyDAO extends DynamoDAO<Reply> {

    /**
     * Save a single reply
     *
     * @param reply The reply to be saved
     */
    public void save(@NonNull final Reply reply) {
        super.put(reply);
    }

    /**
     * List of replies for a post
     *
     * @param messageId the post id
     * @return list of replies
     */
    @NonNull
    public List<Reply> getAllReplies(@NonNull final String messageId) {
        return super.getAll(Reply.class, "rootMessageId,messageId,message,messageDepth,userName,city", new Reply(messageId), true);
    }

    /**
     * A single reply for a post and reply id
     *
     * @param postId  the post id
     * @param replyId the reply id
     * @return Reply retrieved
     */
    @Nullable
    public Reply getReply(@NonNull final String postId, @NonNull final String replyId) {
        return super.get(postId, replyId, Reply.class);
    }
}
