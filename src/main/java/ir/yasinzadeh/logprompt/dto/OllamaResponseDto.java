package ir.yasinzadeh.logprompt.dto;

/**
 * @author Mahdi Yasinzadeh
 * @since 7/3/25
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OllamaResponseDto {
    private String model;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSSX")
    private LocalDateTime created_at;

    private String response;
    private boolean done;
}
