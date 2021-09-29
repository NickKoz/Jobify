package com.webdev.jobify.services;


import com.webdev.jobify.model.Message;
import com.webdev.jobify.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    public final MessageRepo messageRepo;

    @Autowired
    public MessageService(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    public Message findMessageById(Long id) {
        return messageRepo.findMessageById(id);
    }

    public List<Message> findAllMessages() {
        return messageRepo.findAll();
    }

    public Message addMessage(Message message) {
        return messageRepo.save(message);
    }

    public List<Message> findMessagesOfEmployee(Long id) {
        return messageRepo.findMessagesOfEmployee(id);
    }
}
