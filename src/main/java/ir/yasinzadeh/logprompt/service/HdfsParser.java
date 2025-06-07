package ir.yasinzadeh.logprompt.service;

import ir.yasinzadeh.logprompt.dto.ChatGPTRequest;
import ir.yasinzadeh.logprompt.dto.ChatGptResponse;
import ir.yasinzadeh.logprompt.dto.LogHdfsEntryDto;
import ir.yasinzadeh.logprompt.dto.Message;
//import ir.yasinzadeh.logprompt.entity.FinalPrompts;
import ir.yasinzadeh.logprompt.entity.PromptDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mahdi Yasinzadeh
 * @since 5/29/25
 */

@Service
public class HdfsParser {
    @Value("${openai.model}")
    private String model;

    @Value(("${openai.api.url}"))
    private String apiURL;

    private final RestTemplate template;
//    private final FinalPromptsService finalPromptsService;

    private static final Pattern LOG_PATTERN = Pattern.compile(
            "^(?<date>\\d{6})\\s+" +
            "(?<time>\\d{6})\\s+" +
            "(?<pid>\\d+)\\s+" +
            "(?<level>[A-Z]+)\\s+" +
            "(?<component>[\\w\\.$]+):\\s+" +
            "(?<message>.*)$"
    );

    public HdfsParser(RestTemplate template) {
        this.template = template;
//        this.finalPromptsService = finalPromptsService;
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
                    processBatch(batch); // ذخیره‌سازی، ساخت پرامپت یا چاپ
                    batch.clear();

                    Thread.sleep(2000); // وقفه اختیاری
                }
            }

            // در صورت باقی‌ماندن لاگ کمتر از ۱۰ عدد در آخر فایل
            if (!batch.isEmpty()) {
                processBatch(batch);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void processBatch(List<LogHdfsEntryDto> batch) {
        List<PromptDto> prompts = new ArrayList<>();
        batch.forEach(dto -> {
            PromptGenerator.generatePromptsHdfs(dto)
                    .forEach(prompt -> prompts.add(new PromptDto()
                            .setPrompt(prompt)
                            .setResult(getResultAi(prompt))));
//            finalPromptsService.save(
//                    new FinalPrompts()
//                            .setPrompts(prompts)
//                            .setLog(dto.getMainLog())
//            );
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

