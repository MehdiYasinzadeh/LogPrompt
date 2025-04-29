package ir.yasinzadeh.logprompt.service;

import org.springframework.stereotype.Component;

import java.util.List;


import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LogReaderBGL {
    public Iterable<List<String>> readLogFileInBatches(String filePath, int batchSize) throws Exception {
        return () -> {
            try {
                return new LogBatchIteratorBGL(filePath, batchSize);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}