package ir.yasinzadeh.logprompt.controller;

import ir.yasinzadeh.logprompt.service.BglParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class BglController {
    private final BglParser bglParser;

    public BglController(BglParser bglParser) {
        this.bglParser = bglParser;
    }

    @GetMapping("/bgl")
    public ResponseEntity<String> bgl() throws IOException {
        bglParser.logParser();
        return ResponseEntity.ok().build();
    }

}
