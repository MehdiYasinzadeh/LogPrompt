package ir.yasinzadeh.logprompt.controller;

import ir.yasinzadeh.logprompt.service.BglParser;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class BglController {
    private final BglParser bglParser;
    @Autowired
    private ChatClient.Builder chatClient;

    public BglController(BglParser bglParser) {
        this.bglParser = bglParser;
    }

    @GetMapping("/bgl")
    public ResponseEntity<String> bgl() throws IOException {
        var client = chatClient.build();
        var response = client.prompt("Tell me an interesting fact about Google")
                .call()
                .content();
        bglParser.logParser();
        return ResponseEntity.ok().build();
    }

}
