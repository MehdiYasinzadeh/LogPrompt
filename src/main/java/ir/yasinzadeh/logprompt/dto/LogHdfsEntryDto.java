package ir.yasinzadeh.logprompt.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Mahdi Yasinzadeh
 * @since 5/29/25
 */

@Getter
@Setter
public class LogHdfsEntryDto {
    private String mainLog;
    private String date;
    private String time;
    private String pid;
    private String level;
    private String component;
    private String message;
}
