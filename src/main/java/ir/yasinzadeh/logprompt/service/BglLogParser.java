package ir.yasinzadeh.logprompt.service;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

public class BglLogParser {

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

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("p:\\payan-nameh\\BGL.log"));

        List<List<String>> chunks = new ArrayList<>();
        for (int i = 0; i < lines.size(); i += 10) {
            chunks.add(lines.subList(i, Math.min(i + 10, lines.size())));
        }

        for (List<String> chunk : chunks) {
            List<LogEntryDto> dtos = chunk.stream()
                    .map(BglLogParser::parseLine)
                    .collect(Collectors.toList());
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            dtos.forEach(System.out::println);
        }
    }

    private static LogEntryDto parseLine(String line) {
        Matcher matcher = LOG_PATTERN.matcher(line);
        if (matcher.matches()) {
            return new LogEntryDto(
                    Optional.ofNullable(matcher.group("label")),
                    Optional.ofNullable(matcher.group("timestamp")),
                    Optional.ofNullable(matcher.group("date")),
                    Optional.ofNullable(matcher.group("location1")),
                    Optional.ofNullable(matcher.group("datetime")),
                    Optional.ofNullable(matcher.group("location2")),
                    Optional.ofNullable(matcher.group("category")),
                    Optional.ofNullable(matcher.group("component")),
                    Optional.ofNullable(matcher.group("severity")),
                    Optional.ofNullable(matcher.group("message"))
            );
        } else {
            return new LogEntryDto();
        }
    }

    public static class LogEntryDto {
        Optional<String> label = Optional.empty();
        Optional<String> timestamp = Optional.empty();
        Optional<String> date = Optional.empty();
        Optional<String> location1 = Optional.empty();
        Optional<String> datetime = Optional.empty();
        Optional<String> location2 = Optional.empty();
        Optional<String> category = Optional.empty();
        Optional<String> component = Optional.empty();
        Optional<String> severity = Optional.empty();
        Optional<String> message = Optional.empty();

        public LogEntryDto() {}

        public LogEntryDto(Optional<String> label, Optional<String> timestamp, Optional<String> date,
                           Optional<String> location1, Optional<String> datetime, Optional<String> location2,
                           Optional<String> category, Optional<String> component, Optional<String> severity,
                           Optional<String> message) {
            this.label = label;
            this.timestamp = timestamp;
            this.date = date;
            this.location1 = location1;
            this.datetime = datetime;
            this.location2 = location2;
            this.category = category;
            this.component = component;
            this.severity = severity;
            this.message = message;
        }

        @Override
        public String toString() {
            return "LogEntryDto{" +
                    "label=" + label +
                    ", timestamp=" + timestamp +
                    ", date=" + date +
                    ", location1=" + location1 +
                    ", datetime=" + datetime +
                    ", location2=" + location2 +
                    ", category=" + category +
                    ", component=" + component +
                    ", severity=" + severity +
                    ", message=" + message +
                    '}';
        }
    }
}
