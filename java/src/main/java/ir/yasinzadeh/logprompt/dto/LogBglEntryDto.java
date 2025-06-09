package ir.yasinzadeh.logprompt.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LogBglEntryDto {
    private String mainLog;
    private String label;
    private String timestamp;
    private String date;
    private String location1;
    private String datetime;
    private String location2;
    private String category;
    private String component;
    private String severity;
    private String message;

    public String getMainLog() {
        return mainLog;
    }

    public LogBglEntryDto setMainLog(String mainLog) {
        this.mainLog = mainLog;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public LogBglEntryDto setLabel(String label) {
        this.label = label;
        return this;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public LogBglEntryDto setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getDate() {
        return date;
    }

    public LogBglEntryDto setDate(String date) {
        this.date = date;
        return this;
    }

    public String getLocation1() {
        return location1;
    }

    public LogBglEntryDto setLocation1(String location1) {
        this.location1 = location1;
        return this;
    }

    public String getDatetime() {
        return datetime;
    }

    public LogBglEntryDto setDatetime(String datetime) {
        this.datetime = datetime;
        return this;
    }

    public String getLocation2() {
        return location2;
    }

    public LogBglEntryDto setLocation2(String location2) {
        this.location2 = location2;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public LogBglEntryDto setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getComponent() {
        return component;
    }

    public LogBglEntryDto setComponent(String component) {
        this.component = component;
        return this;
    }

    public String getSeverity() {
        return severity;
    }

    public LogBglEntryDto setSeverity(String severity) {
        this.severity = severity;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public LogBglEntryDto setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
          return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
