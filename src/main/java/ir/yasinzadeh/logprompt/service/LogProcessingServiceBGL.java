package ir.yasinzadeh.logprompt.service;

import ir.yasinzadeh.logprompt.dto.LogEntryBGL;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogProcessingServiceBGL {
    private final LogReaderBGL logReader;
    private final LogCleanerBGL logCleaner;
    private final LogTokenizerBGL logTokenizer;

    public LogProcessingServiceBGL(LogReaderBGL logReader, LogCleanerBGL logCleaner, LogTokenizerBGL logTokenizer) {
        this.logReader = logReader;
        this.logCleaner = logCleaner;
        this.logTokenizer = logTokenizer;
    }

    public Iterable<List<List<String>>> processLogsInBatches(String filePath, int batchSize) throws Exception {
        Iterable<List<String>> batches = logReader.readLogFileInBatches(filePath, batchSize);
        return () -> new Iterator<List<List<String>>>() {
            private final Iterator<List<String>> batchIterator = batches.iterator();

            @Override
            public boolean hasNext() {
                return batchIterator.hasNext();
            }

            @Override
            public List<List<String>> next() {
                List<String> batch = batchIterator.next();
                List<LogEntryBGL> cleanedLogs = logCleaner.cleanLogs(batch);
                return cleanedLogs.stream()
                        .map(logTokenizer::tokenize)
                        .collect(Collectors.toList());
            }
        };
    }
}