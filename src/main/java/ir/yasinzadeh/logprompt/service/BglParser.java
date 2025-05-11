package ir.yasinzadeh.logprompt.service;

import ir.yasinzadeh.logprompt.dto.LogBglEntryDto;
import ir.yasinzadeh.logprompt.entity.BglPrompts;
import ir.yasinzadeh.logprompt.entity.PromptDto;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BglParser {

    private final ChatClient chatClient;
    private final BglPromptsService bglPromptsService;
    private final PromptGenerator promptGenerator;

    static final Pattern LOG_PATTERN = Pattern.compile(
            "(?<label>-)?\\s*" +
                    "(?<timestamp>\\d+)\\s+" +
                    "(?<date>\\d{4}\\.\\d{2}\\.\\d{2})\\s+" +
                    "(?<location1>R\\d+-M\\d+-N\\d+-C:J\\d+-U\\d+)\\s+" +
                    "(?<datetime>\\d{4}-\\d{2}-\\d{2}-\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d+)\\s+" +
                    "(?<location2>R\\d+-M\\d+-N\\d+-C:J\\d+-U\\d+)\\s+" +
                    "(?<category>[A-Z]+)\\s+" +
                    "(?<component>[A-Z]+)\\s+" +
                    "(?<severity>[A-Z]+)\\s+" +
                    "(?<message>.*)");

    public BglParser(PromptGenerator promptGenerator, BglPromptsService bglPromptsService, ChatClient.Builder chatClientBuilder) {
        this.promptGenerator = promptGenerator;
        this.bglPromptsService = bglPromptsService;
        this.chatClient = chatClientBuilder.build();
    }

    public void logParser() throws IOException {
        List<String> lines = Files.readAllLines(Path.of("p:\\payan-nameh\\BGL.log"));

        List<List<String>> chunks = new ArrayList<>();
        for (int i = 0; i < lines.size(); i += 10) {
            chunks.add(lines.subList(i, Math.min(i + 10, lines.size())));
        }

        for (List<String> chunk : chunks) {
            List<LogBglEntryDto> dtos = chunk.stream()
                    .map(BglParser::parseLine)
                    .toList();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            makeBglPrompt(dtos);
        }
    }

    private void makeBglPrompt(List<LogBglEntryDto> dtos) {
        List<PromptDto> prompts = new ArrayList<>();
        dtos.forEach(dto -> {
            promptGenerator.generatePrompts(dto)
                    .forEach(prompt -> prompts.add(new PromptDto()
                            .setPrompt(prompt)
                            .setResult(this.chatClient.prompt()
                                    .user(prompt)
                                    .call()
                                    .content())));
            bglPromptsService.save(
                    new BglPrompts()
                            .setPrompts(prompts)
                            .setLog(dto.getMainLog())
            );
        });
    }

    private static LogBglEntryDto parseLine(String line) {
        Matcher matcher = LOG_PATTERN.matcher(line);
        if (matcher.matches()) {
            return new LogBglEntryDto()
                    .setMainLog(line)
                    .setLabel(matcher.group("label"))
                    .setTimestamp(matcher.group("timestamp"))
                    .setDate(matcher.group("date"))
                    .setLocation1(matcher.group("location1"))
                    .setDatetime(matcher.group("datetime"))
                    .setLocation2(matcher.group("location2"))
                    .setCategory(matcher.group("category"))
                    .setComponent(matcher.group("component"))
                    .setSeverity(matcher.group("severity"))
                    .setMessage(matcher.group("message"));
        } else {
            return new LogBglEntryDto();
        }
    }

}
