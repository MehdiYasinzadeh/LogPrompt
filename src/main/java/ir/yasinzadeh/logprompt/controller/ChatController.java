package ir.yasinzadeh.logprompt.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {
    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("You are an AI assistant answering questions about different products")
                .build();
    }

    @GetMapping("/ai")
    public ResponseEntity<String> generate() {
        try {
            String response = chatClient.prompt()
                    .user("tell me a jok")
                    .call()
                    .content();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }
}
