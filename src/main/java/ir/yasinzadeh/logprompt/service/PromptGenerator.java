package ir.yasinzadeh.logprompt.service;

import ir.yasinzadeh.logprompt.dto.LogBglEntryDto;
import ir.yasinzadeh.logprompt.dto.LogHdfsEntryDto;

import java.util.List;
import java.util.Optional;

public class PromptGenerator {

    public static List<String> generatePromptsBgl(LogBglEntryDto dto) {
        String sem = Optional.ofNullable(dto.getMessage()).orElse("").trim();
        String seq = String.join(" ",
                Optional.ofNullable(dto.getTimestamp()).orElse(""),
                Optional.ofNullable(dto.getDate()).orElse(""),
                Optional.ofNullable(dto.getLocation1()).orElse(""),
                Optional.ofNullable(dto.getDatetime()).orElse(""),
                Optional.ofNullable(dto.getLocation2()).orElse(""),
                Optional.ofNullable(dto.getCategory()).orElse(""),
                Optional.ofNullable(dto.getComponent()).orElse(""),
                Optional.ofNullable(dto.getSeverity()).orElse("")
        ).trim();

        String prompt = "Answer with only one character: either 0 (normal) or 1 (anomaly). Do not explain. Just output the digit.\n";
        String prompt1 = prompt + String.format("semantic %s sequential %s it is [MSK]", sem, seq);
        String prompt2 = prompt + String.format("%s %s it is [MSK]", sem, seq);
        String prompt3 = prompt + String.format("%s %s normal or anomaly ? [MSK]", sem, seq);
        String prompt4 = prompt + String.format("%s %s h[PRO] h[PRO] [MSK]", sem, seq);

        return List.of(prompt1, prompt2, prompt3, prompt4);
    }

    public static List<String> generatePromptsHdfs(LogHdfsEntryDto dto) {
        String sem = Optional.ofNullable(dto.getMessage()).orElse("").trim();
        String seq = String.join(" ",
                Optional.ofNullable(dto.getDate()).orElse(""),
                Optional.ofNullable(dto.getTime()).orElse(""),
                Optional.ofNullable(dto.getPid()).orElse(""),
                Optional.ofNullable(dto.getLevel()).orElse(""),
                Optional.ofNullable(dto.getComponent()).orElse("")
        ).trim();

        String prompt = "Answer with only one character: either 0 (normal) or 1 (anomaly). Do not explain. Just output the digit.\n";
        String prompt1 = prompt + String.format("semantic %s sequential %s it is [MSK]", sem, seq);
        String prompt2 = prompt + String.format("%s %s it is [MSK]", sem, seq);
        String prompt3 = prompt + String.format("%s %s normal or anomaly ? [MSK]", sem, seq);
        String prompt4 = prompt + String.format("%s %s h[PRO] h[PRO] [MSK]", sem, seq);

        return List.of(prompt1, prompt2, prompt3, prompt4);
    }

}

