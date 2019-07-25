package com.loyalty.homework.services;

import com.loyalty.homework.dao.MessageDAO;
import com.loyalty.homework.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private MessageDAO messageDAO;

    public Message saveMessage(final String message) {
        final Message messageDTO = new Message(message);
        messageDAO.put(messageDTO);
        return messageDTO;
    }

}
