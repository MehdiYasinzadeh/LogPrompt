package ir.yasinzadeh.logprompt.service;

import ir.yasinzadeh.logprompt.dto.LogHdfsEntryDto;
import ir.yasinzadeh.logprompt.entity.AiModel;
import ir.yasinzadeh.logprompt.entity.FinalPrompts;
import ir.yasinzadeh.logprompt.entity.LogType;
import ir.yasinzadeh.logprompt.entity.PromptDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mahdi Yasinzadeh
 * @since 5/29/25
 */

@Service
public class HdfsParser {

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

    private static final Pattern LOG_PATTERN = Pattern.compile(
            "^(?<date>\\d{6})\\s+" +
            "(?<time>\\d{6})\\s+" +
            "(?<pid>\\d+)\\s+" +
            "(?<level>[A-Z]+)\\s+" +
            "(?<component>[\\w\\.$]+):\\s+" +
            "(?<message>.*)$"
    );

    public HdfsParser(CallModelAi callModelAi, FinalPromptsService finalPromptsService) {
        this.callModelAi = callModelAi;
        this.finalPromptsService = finalPromptsService;
    }


    public void parseHdfsLogsEfficient(String filePath) {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(filePath))) {
            List<LogHdfsEntryDto> batch = new ArrayList<>();
            String line;
            int lineCount = 0;

            while ((line = reader.readLine()) != null) {
                LogHdfsEntryDto dto = parseLine(line);
                batch.add(dto);
                lineCount++;
                if (lineCount % 10 == 0) {
                    processBatch(batch);
                    batch.clear();
                }
            }
            if (!batch.isEmpty()) {
                processBatch(batch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processBatch(List<LogHdfsEntryDto> batch) {
        batch.forEach(dto -> {
            List<PromptDto> gptPrompts = new ArrayList<>();
            PromptGenerator.generatePromptsHdfs(dto)
                    .forEach(prompt -> gptPrompts.add(new PromptDto()
                            .setPrompt(prompt)
                            .setResult(callModelAi.getGptResult(gptModel, prompt, gptApiURL))
                            .setAiModel(AiModel.CHATGPT)));
            finalPromptsService.save(new FinalPrompts()
                    .setPrompts(gptPrompts)
                    .setLog(dto.getMainLog())
                    .setLogType(LogType.HDFS));

            List<PromptDto> ollamaPrompts = new ArrayList<>();
            PromptGenerator.generatePromptsHdfs(dto)
                    .forEach(prompt -> ollamaPrompts.add(new PromptDto()
                            .setPrompt(prompt)
                            .setResult(callModelAi.getGptResult(gptModel, prompt, gptApiURL))
                            .setAiModel(AiModel.OLLAMMA)));
            finalPromptsService.save(new FinalPrompts()
                    .setPrompts(ollamaPrompts)
                    .setLog(dto.getMainLog())
                    .setLogType(LogType.HDFS));

            List<PromptDto> bertPrompts = new ArrayList<>();
            PromptGenerator.generatePromptsHdfs(dto)
                    .forEach(prompt -> bertPrompts.add(new PromptDto()
                            .setPrompt(prompt)
                            .setResult(callModelAi.getGptResult(gptModel, prompt, gptApiURL))
                            .setAiModel(AiModel.BERT)));
            finalPromptsService.save(new FinalPrompts()
                    .setPrompts(bertPrompts)
                    .setLog(dto.getMainLog())
                    .setLogType(LogType.HDFS));

            List<PromptDto> robertaPrompts = new ArrayList<>();
            PromptGenerator.generatePromptsHdfs(dto)
                    .forEach(prompt -> robertaPrompts.add(new PromptDto()
                            .setPrompt(prompt)
                            .setResult(callModelAi.getGptResult(gptModel, prompt, gptApiURL))
                            .setAiModel(AiModel.ROBERTA)));
            finalPromptsService.save(new FinalPrompts()
                    .setPrompts(robertaPrompts)
                    .setLog(dto.getMainLog())
                    .setLogType(LogType.HDFS));

            List<PromptDto> albertPrompts = new ArrayList<>();
            PromptGenerator.generatePromptsHdfs(dto)
                    .forEach(prompt -> albertPrompts.add(new PromptDto()
                            .setPrompt(prompt)
                            .setResult(callModelAi.getGptResult(gptModel, prompt, gptApiURL))
                            .setAiModel(AiModel.ALBERTA)));
            finalPromptsService.save(new FinalPrompts()
                    .setPrompts(albertPrompts)
                    .setLog(dto.getMainLog())
                    .setLogType(LogType.HDFS));
        });
    }

    private LogHdfsEntryDto parseLine(String line) {
        Matcher matcher = LOG_PATTERN.matcher(line);
        LogHdfsEntryDto dto = new LogHdfsEntryDto();
        dto.setMainLog(line);

        if (matcher.matches()) {
            dto.setDate(matcher.group("date"));
            dto.setTime(matcher.group("time"));
            dto.setPid(matcher.group("pid"));
            dto.setLevel(matcher.group("level"));
            dto.setComponent(matcher.group("component"));
            dto.setMessage(matcher.group("message"));
        }

        return dto;
    }
}

