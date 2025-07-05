package ir.yasinzadeh.logprompt.service;

import ir.yasinzadeh.logprompt.dto.LogBglEntryDto;
import ir.yasinzadeh.logprompt.entity.AiModel;
import ir.yasinzadeh.logprompt.entity.FinalPrompts;
import ir.yasinzadeh.logprompt.entity.PromptDto;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${model.api.gpt.url}")
    private String gptApiURL;

    @Value("${model.api.ollama.url}")
    private String ollamaApiUrl;

    @Value("${model.api.gpt.model-name}")
    private String gptModel;

    @Value("${model.api.ollama.model-name}")
    private String ollamaModel;


    private final CallModelAi callModelAi;
    private final FinalPromptsService finalPromptsService;

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

    public BglParser(CallModelAi callModelAi, FinalPromptsService finalPromptsService) {
        this.callModelAi = callModelAi;
        this.finalPromptsService = finalPromptsService;
    }


    public void logParser() throws IOException {
        List<String> lines = Files.readAllLines(Path.of("/home/mehdi/Downloads/BGL/BGL.log"));

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
            makeAndSaveBglPrompt(dtos);
        }
    }

    private void makeAndSaveBglPrompt(List<LogBglEntryDto> dtos) {
        dtos.forEach(dto -> {

            List<PromptDto> ollamaPromps = new ArrayList<>();
            PromptGenerator.generatePromptsBgl(dto)
                    .forEach(prompt -> setOllamaPromps(prompt, ollamaPromps));
            finalPromptsService.save(new FinalPrompts().setPrompts(ollamaPromps).setLog(dto.getMainLog()));

            List<PromptDto> gptPromps = new ArrayList<>();
            PromptGenerator.generatePromptsBgl(dto)
                    .forEach(prompt -> setGptPromps(prompt, gptPromps));
            finalPromptsService.save(new FinalPrompts().setPrompts(gptPromps).setLog(dto.getMainLog()));

        });
    }

    private void setOllamaPromps(String prompt, List<PromptDto> ollamaPromps) {
        ollamaPromps.add(new PromptDto()
                .setPrompt(prompt)
                .setResult(callModelAi.getOllamaResult(ollamaModel, prompt, ollamaApiUrl))
                .setAiModel(AiModel.OLLAMMA));
    }

    private void setGptPromps(String prompt, List<PromptDto> gptPrompts) {
        gptPrompts.add(new PromptDto()
                .setPrompt(prompt)
                .setResult(callModelAi.getGptResult(gptModel, prompt, gptApiURL))
                .setAiModel(AiModel.CHATGPT));
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
