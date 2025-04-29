package ir.yasinzadeh.logprompt.controller;

import ir.yasinzadeh.logprompt.service.LogProcessingServiceBGL;
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
    private final LogProcessingServiceBGL logProcessingService;

    public ChatController(ChatClient.Builder builder, LogProcessingServiceBGL logProcessingService) {
        this.chatClient = builder
                .defaultSystem("You are an AI assistant answering questions about different products")
                .build();
        this.logProcessingService = logProcessingService;
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

    @GetMapping("/bgl")
    public ResponseEntity<String> bgl() {
        try {
            int batchSize = 10;
            Iterable<List<List<String>>> tokenizedBatches = logProcessingService.processLogsInBatches("D:\\payan-nameh\\BGL.log", batchSize);
            int batchNumber = 1;
            for (List<List<String>> tokenizedBatch : tokenizedBatches) {
                System.out.println("Batch " + batchNumber + ":");
                tokenizedBatch.forEach(tokens -> System.out.println("  Tokens: " + tokens));
                batchNumber++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/file")
    public ResponseEntity<String> processLogFile() {
        Path filePath = Paths.get("D:\\payan-nameh\\BGL.log");
        List<LogTokenizer.LogToken> batch = new ArrayList<>();
        final int BATCH_SIZE = 5;

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            processLogLines(reader, batch, BATCH_SIZE);
            printRemainingBatch(batch);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            throw new RuntimeException("Failed to process log file", e);
        }
    }

    private void processLogLines(BufferedReader reader, List<LogTokenizer.LogToken> batch, int batchSize)
            throws IOException {
        String line;
        int count = 0;

        while ((line = reader.readLine()) != null) {
            LogTokenizer.LogToken token = parseLine(line);
            if (token != null) {
                batch.add(token);
                count++;
            }

            if (count == batchSize) {
                printBatch(batch);
                batch.clear();
                count = 0;
            }
        }
    }

    private void printBatch(List<LogTokenizer.LogToken> batch) {
        System.out.println("--------- Batch ---------");
        batch.forEach(System.out::println);
    }

    private void printRemainingBatch(List<LogTokenizer.LogToken> batch) {
        if (!batch.isEmpty()) {
            System.out.println("--------- Last Batch ---------");
            batch.forEach(System.out::println);
        }
    }

}
