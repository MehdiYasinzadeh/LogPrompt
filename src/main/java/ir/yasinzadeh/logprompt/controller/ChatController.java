package ir.yasinzadeh.logprompt.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        String path = "P:\\payan-nameh\\HDFS.log";

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            List<String> lines = new ArrayList<>();
            int batchSize = 10;

            while ((line = reader.readLine()) != null) {
                lines.add(line);

                if (lines.size() == batchSize) {
                    // چاپ دسته 10تایی
                    lines.forEach(System.out::println);
                    System.out.println("-------------- Batch Separator --------------");
                    Thread.sleep(1000);
                    lines.clear();
                }
            }

            if (!lines.isEmpty()) {
                lines.forEach(System.out::println);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("خطا در خواندن فایل: " + e.getMessage());
        }

        return ResponseEntity.ok("Done!");
    }


}
