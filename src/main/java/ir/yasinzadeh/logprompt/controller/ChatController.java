package ir.yasinzadeh.logprompt.controller;

import ir.yasinzadeh.logprompt.service.LogTokenizer;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static ir.yasinzadeh.logprompt.service.LogTokenizer.parseLine;

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

    @GetMapping("/file")
    public ResponseEntity<String> log() {
        Path filePath = Paths.get("P:/payan-nameh/HDFS.log");
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            int count = 0;
            List<LogTokenizer.LogToken> batch = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                LogTokenizer.LogToken token = parseLine(line);
                if (token != null) {
                    batch.add(token);
                    count++;
                }

                if (count == 10) {
                    System.out.println("--------- Batch ---------");
                    for (LogTokenizer.LogToken t : batch) {
                        System.out.println(t);
                    }
                    batch.clear();
                    count = 0;
                }
            }

            // Remaining lines
            if (!batch.isEmpty()) {
                System.out.println("--------- Last Batch ---------");
                for (LogTokenizer.LogToken t : batch) {
                    System.out.println(t);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok().build();

    }

}
