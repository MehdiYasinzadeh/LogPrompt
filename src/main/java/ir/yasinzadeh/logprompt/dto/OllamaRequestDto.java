package ir.yasinzadeh.logprompt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Mahdi Yasinzadeh
 * @since 7/3/25
 */

@Getter
@Setter
@AllArgsConstructor
public class OllamaRequestDto {
    private String model;
    private String prompt;
    private Options options;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Options {
        private double temperature;
        private int num_predict;
    }
}

