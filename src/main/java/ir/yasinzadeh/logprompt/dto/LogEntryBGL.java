package ir.yasinzadeh.logprompt.dto;

import lombok.Data;

@Data
public class LogEntryBGL {
    private String timestamp;
    private String node;
    private String severity;
    private String message;
}
