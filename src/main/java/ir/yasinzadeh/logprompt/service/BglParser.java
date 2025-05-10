package ir.yasinzadeh.logprompt.service;

import ir.yasinzadeh.logprompt.dto.LogBglEntryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public BglParser(PromptGenerator promptGenerator) {
        this.promptGenerator = promptGenerator;
    }

    public void logParser() throws IOException {
        List<String> lines = Files.readAllLines(Path.of("D:\\payan-nameh\\BGL.log"));

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
            dtos.forEach(System.out::println);
            //make your prompt
             String prompt = makePrompt(dtos);
            //send 10 log to model
            //save response of model
        }
    }
    private String makePrompt(List<LogBglEntryDto> dtos) {
        StringBuilder prompt = new StringBuilder();
        dtos.forEach(dto ->{
            List<String> prompts = promptGenerator.generatePrompts(dto);
            prompts.forEach(prompt::append);
        });
        return prompt.toString();
    }

    private static LogBglEntryDto parseLine(String line) {
        Matcher matcher = LOG_PATTERN.matcher(line);
        if (matcher.matches()) {
            return new LogBglEntryDto()
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
