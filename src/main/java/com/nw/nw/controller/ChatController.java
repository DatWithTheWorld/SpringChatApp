package com.nw.nw.controller;

import com.nw.nw.model.ChatMessage;
import com.nw.nw.model.OnlineUserManager;
import com.nw.nw.model.User;
import com.nw.nw.service.ChatService;
import com.nw.nw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Set;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;
    @Autowired
    private UserService userService;
    @Autowired
    private OnlineUserManager onlineUserManager;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        if (user.getProfileImage() != null) {
            chatMessage.setProfileImage(user.getProfileImage());
        }
        chatService.saveMessage(chatMessage);
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        String username = chatMessage.getSender();
        headerAccessor.getSessionAttributes().put("username", username);
        onlineUserManager.addUser(username);
        broadcastOnlineUsers();
        return chatMessage;
    }

    private void broadcastOnlineUsers() {
        Set<String> onlineUsers = onlineUserManager.getOnlineUsers();
        messagingTemplate.convertAndSend("/topic/onlineUsers", onlineUsers);
    }

    @MessageMapping("/chat.removeUser")
    @SendTo("/topic/public")
    public ChatMessage removeUser(@Payload ChatMessage chatMessage,
                                  SimpMessageHeaderAccessor headerAccessor) {
        String username = (String) headerAccessor.getSessionAttributes().remove("username");
        onlineUserManager.removeUser(username);
        return chatMessage;
    }

    @MessageMapping("/chat.getOnlineUsers")
    @SendTo("/topic/onlineUsers")
    public Set<String> getOnlineUsers() {
        return onlineUserManager.getOnlineUsers();
    }
}
