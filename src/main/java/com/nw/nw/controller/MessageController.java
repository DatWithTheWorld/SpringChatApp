package com.nw.nw.controller;

import com.nw.nw.model.ChatMessage;
import com.nw.nw.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class MessageController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/messages")
    public List<ChatMessage> getMessages() {
        return chatService.getAllMessages();
    }
}
