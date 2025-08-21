package ir.yasinzadeh.logprompt.controller;

import ir.yasinzadeh.logprompt.service.HdfsParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class HdfsController {

    @Value("${hdfs.location}")
    private String hdfsPath;

    private final HdfsParser hdfsParser;

    public HdfsController(HdfsParser hdfsParser) {
        this.hdfsParser = hdfsParser;
    }

    @GetMapping("/hdfs")
    public ResponseEntity<String> bgl() throws IOException {
        hdfsParser.parseHdfsLogsEfficient(hdfsPath);
        return ResponseEntity.ok().build();
    }

}
