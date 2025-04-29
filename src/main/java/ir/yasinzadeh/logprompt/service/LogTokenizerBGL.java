package ir.yasinzadeh.logprompt.service;

import ir.yasinzadeh.logprompt.dto.LogEntryBGL;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LogTokenizerBGL {
    public List<String> tokenize(LogEntryBGL logEntry) {
        // شکستن پیام به کلمات
        String message = logEntry.getMessage().toLowerCase();
        // حذف کاراکترهای خاص و جایگزینی اعداد با توکن عمومی
        message = message.replaceAll("\\d+", "<NUM>");
        // شکستن به کلمات و حذف کلمات توقف
        List<String> tokens = Arrays.stream(message.split("\\s+"))
                .filter(token -> !token.isEmpty() && !isStopWord(token))
                .collect(Collectors.toList());
        return tokens;
    }

    private boolean isStopWord(String word) {
        // لیست ساده کلمات توقف
        List<String> stopWords = Arrays.asList("is", "the", "a", "an");
        return stopWords.contains(word);
    }
}