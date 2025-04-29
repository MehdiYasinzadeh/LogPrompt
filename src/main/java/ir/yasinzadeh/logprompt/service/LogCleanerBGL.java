package ir.yasinzadeh.logprompt.service;

import ir.yasinzadeh.logprompt.dto.LogEntryBGL;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class LogCleanerBGL {
    private static final Pattern LOG_PATTERN = Pattern.compile(
            "^(\\d{4}\\.\\d{2}\\.\\d{2})\\s+(\\S+)\\s+(\\d{4}-\\d{2}-\\d{2}-\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{6})\\s+(\\w+)\\s+(\\w+)\\s+(.+)$"
    );

    public List<LogEntryBGL> cleanLogs(List<String> rawLogs) {
        List<LogEntryBGL> cleanedLogs = new ArrayList<>();
        for (String log : rawLogs) {
            if (log == null || log.trim().isEmpty()) {
                System.out.println("Skipping empty or null log line");
                continue;
            }
            // پیش‌پردازش برای حذف نویز
            String preprocessedLog = preprocessLogLine(log);
            Matcher matcher = LOG_PATTERN.matcher(preprocessedLog);
            if (matcher.matches()) {
                LogEntryBGL entry = new LogEntryBGL();
                entry.setTimestamp(matcher.group(3));
                entry.setNode(matcher.group(2));
                entry.setSeverity(matcher.group(5));
                entry.setMessage(matcher.group(6).trim());
                cleanedLogs.add(entry);
            } else {
                System.out.println("Log line did not match pattern: "+ preprocessedLog);
            }
        }
        return cleanedLogs;
    }

    private String preprocessLogLine(String logLine) {
        // حذف فضاهای اضافی ابتدا و انتها
        String cleaned = logLine.trim();

        // جایگزینی چندین فضای خالی با یک فضای خالی
        cleaned = cleaned.replaceAll("\\s+", " ");

        // حذف کاراکترهای خاص غیرضروری (مثل کاراکترهای کنترلی یا علامت‌های نادرست)
        cleaned = cleaned.replaceAll("[\\p{Cntrl}\\t\\r\\n]", "");

        // استانداردسازی قالب‌های ناسازگار (مثل تبدیل چندین - به یک -)
        cleaned = cleaned.replaceAll("-{2,}", "-");

        // حذف کاراکترهای خاص در جاهای غیرمنتظره (مثل ; یا ,)
        cleaned = cleaned.replaceAll("[;,!@#$%^&*()+=|<>?{}\\[\\]~`]", "");

        // لاگ کردن خط پاک‌شده برای دیباگ
        log.debug("Preprocessed log line: {}", cleaned);

        return cleaned;
    }
}
