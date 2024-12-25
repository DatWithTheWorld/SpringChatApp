package com.nw.nw.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nw.nw.model.ChatMessage;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {
    private final String MESSAGE_FILE = "messages.json";
    private final ObjectMapper mapper = new ObjectMapper();

    public void saveMessage(ChatMessage message) {
        try {
            List<ChatMessage> messages = getAllMessages();
            messages.add(message);
            mapper.writeValue(new File(MESSAGE_FILE), messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<ChatMessage> getAllMessages() {
        try {
            File file = new File(MESSAGE_FILE);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return mapper.readValue(file, new TypeReference<List<ChatMessage>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}