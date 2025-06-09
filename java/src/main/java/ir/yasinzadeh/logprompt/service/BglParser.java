package ir.yasinzadeh.logprompt.service;

import ir.yasinzadeh.logprompt.dto.ChatGPTRequest;
import ir.yasinzadeh.logprompt.dto.ChatGptResponse;
import ir.yasinzadeh.logprompt.dto.LogBglEntryDto;
import ir.yasinzadeh.logprompt.dto.Message;
//import ir.yasinzadeh.logprompt.entity.FinalPrompts;
import ir.yasinzadeh.logprompt.entity.FinalPrompts;
import ir.yasinzadeh.logprompt.entity.PromptDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BglParser {

    @Value("${openai.model}")
    private String model;

    @Value(("${openai.api.url}"))
    private String apiURL;

    private final FinalPromptsService finalPromptsService;
    private final RestTemplate template;

    public BglParser( RestTemplate template , FinalPromptsService finalPromptsService) {
        this.finalPromptsService = finalPromptsService;
        this.template = template;
    }

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

        dtos.forEach(dto -> {
            List<PromptDto> prompts = new ArrayList<>();
            PromptGenerator.generatePromptsBgl(dto)
                    .forEach(prompt -> prompts.add(new PromptDto()
                            .setPrompt(prompt)
                            .setResult(getResultAi(prompt))));
            finalPromptsService.save(
                    new FinalPrompts()
                            .setPrompts(prompts)
                            .setLog(dto.getMainLog())
            );
        });
    }

    private String getResultAi(String prompt) {
        ChatGPTRequest request = new ChatGPTRequest(model, prompt);

        try {
            ChatGptResponse response = template.postForObject(apiURL, request, ChatGptResponse.class);

            return Optional.ofNullable(response)
                    .map(ChatGptResponse::getChoices)
                    .filter(choices -> !choices.isEmpty())
                    .map(choices -> choices.get(0))
                    .map(ChatGptResponse.Choice::getMessage)
                    .map(Message::getContent)
                    .orElse("An error occurred while communicating with the AI.");
        } catch (Exception e) {
            return "Failed to connect to AI server: " + e.getMessage();
        }
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
