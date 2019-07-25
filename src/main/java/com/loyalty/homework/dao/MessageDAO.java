package com.loyalty.homework.dao;

import com.loyalty.homework.dto.Message;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageDAO extends DynamoDAO<Message> {

    public Message get(final String id, final String range) {
        return super.get(id, range, Message.class);
    }

    Message getOrDefault(String id, String range, Message defaultValue) {
        return super.getOrDefault(id, range, Message.class, defaultValue);
    }

    public List<Message> getAll() {
        return super.getAll(Message.class, "time,message", null);
    }
}
