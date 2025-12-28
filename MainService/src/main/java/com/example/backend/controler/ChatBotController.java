package com.example.backend.controler;

import com.example.backend.model.Quest;
import com.example.backend.service.ChatBotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatBotController {
    private ChatBotService chatBotService;

    public ChatBotController(ChatBotService chatBotService) {
        this.chatBotService = chatBotService;
    }

    @PostMapping("/aichat")
    public ResponseEntity<?> chatBotResponse(@RequestBody Quest text) {
        System.out.println("Tutaj doszedlem do cotrolera " + text.getText());
        return chatBotService.getResponsetoAi(text.getText());
    }
}
