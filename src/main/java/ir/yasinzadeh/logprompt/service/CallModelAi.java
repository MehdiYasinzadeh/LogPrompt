package ir.yasinzadeh.logprompt.service;

import ir.yasinzadeh.logprompt.dto.ChatGPTRequest;
import ir.yasinzadeh.logprompt.dto.ChatGptResponse;
import ir.yasinzadeh.logprompt.dto.Message;
import ir.yasinzadeh.logprompt.dto.OllamaRequestDto;
import ir.yasinzadeh.logprompt.dto.OllamaResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Optional;

/**
 * @author Mahdi Yasinzadeh
 * @since 7/3/25
 */

@Service
@AllArgsConstructor
public class CallModelAi {

    private final RestTemplate template;

    private final WebClient webClient;

    public String getGptResult(String gptModel, String prompt, String apiURL) {
        ChatGPTRequest request = new ChatGPTRequest(gptModel, prompt);

        try {
            ChatGptResponse response = template.postForObject(apiURL, request, ChatGptResponse.class);

            return Optional.ofNullable(response)
                    .map(ChatGptResponse::getChoices)
                    .filter(choices -> !choices.isEmpty())
                    .map(choices -> choices.get(0))
                    .map(ChatGptResponse.Choice::getMessage)
                    .map(Message::getContent)
                    .orElse("-1");
        } catch (Exception e) {
            return "Failed to connect to AI server: " + e.getMessage();
        }
    }

    public String getOllamaResult(String model, String prompt, String apiURL) {
        OllamaRequestDto requestDto = new OllamaRequestDto(
                model, prompt, new OllamaRequestDto.Options(0.2d, 2));
        OllamaResponseDto firstResponseLine = getFirstResponseLine(apiURL, requestDto);
        return firstResponseLine.getResponse().trim();
    }

    public Flux<String> getOllamaResult_v2(String model, String prompt, String apiURL) {
        OllamaRequestDto requestDto = new OllamaRequestDto(
                model, prompt, new OllamaRequestDto.Options(0.8d, 3));
        //getFirstResponseLine(apiURL,requestDto);
        return webClient.method(HttpMethod.POST)
                .uri(apiURL)
                .bodyValue(requestDto)
                .retrieve()
                .bodyToFlux(String.class)
                .doOnNext(str -> {
                    System.out.println(str.trim());
                });
    }

    public OllamaResponseDto getFirstResponseLine(String apiURL, Object requestDto) {
            return webClient.method(HttpMethod.POST)
                    .uri(apiURL)
                    .bodyValue(requestDto)
                    .retrieve()
                    .bodyToFlux(OllamaResponseDto.class).blockFirst();
    }
}
