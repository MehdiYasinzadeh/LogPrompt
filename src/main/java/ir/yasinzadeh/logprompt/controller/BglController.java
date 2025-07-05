package ir.yasinzadeh.logprompt.controller;

import ir.yasinzadeh.logprompt.service.BglParser;
import ir.yasinzadeh.logprompt.service.CallModelAi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;


@RestController
public class BglController {

    @Autowired
    private BglParser bglParser;
    @Autowired
    private CallModelAi callModelAi;

    @Value("${model.api.gpt.url}")
    private String gptApiURL;

    @Value("${model.api.ollama.url}")
    private String ollamaApiUrl;

    @Value("${model.api.gpt.model-name}")
    private String gptModel;

    @Value("${model.api.ollama.model-name}")
    private String ollamaModel;


    @GetMapping("/bgl")
    public ResponseEntity<String> bgl() throws IOException {
        bglParser.logParser();
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/bgl/sse", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Flux<String> bglSse() throws IOException {
        return Flux.from(callModelAi.getOllamaResult_v2(ollamaModel, "what is java dude?", ollamaApiUrl));
    }

}
