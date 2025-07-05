package ir.yasinzadeh.logprompt.dto;

/**
 * @author Mahdi Yasinzadeh
 * @since 7/3/25
 */

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class OllamaResponseDto {
    private String model;
    private OffsetDateTime created_at;
    private String response;
    private boolean done;
}
