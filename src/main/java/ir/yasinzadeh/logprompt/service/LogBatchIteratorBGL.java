package ir.yasinzadeh.logprompt.service;

import lombok.extern.slf4j.Slf4j;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class LogBatchIteratorBGL implements Iterator<List<String>> {
    private final BufferedReader reader;
    private final int batchSize;
    private boolean hasMoreLines;

    public LogBatchIteratorBGL(String filePath, int batchSize) throws Exception {
        this.reader = new BufferedReader(new FileReader(filePath));
        this.batchSize = batchSize;
        this.hasMoreLines = true;
    }

    @Override
    public boolean hasNext() {
        return hasMoreLines;
    }

    @Override
    public List<String> next() {
        List<String> batch = new ArrayList<>();
        try {
            String line;
            for (int i = 0; i < batchSize && (line = reader.readLine()) != null; i++) {
                batch.add(line);
            }
            if (batch.isEmpty()) {
                hasMoreLines = false;
                closeReader();
            } else if (batch.size() < batchSize) {
                hasMoreLines = false; // خطوط باقی‌مانده کمتر از batchSize هستند
            }
            return batch;
        } catch (Exception e) {
            log.error("Error reading batch from file", e);
            hasMoreLines = false;
            closeReader();
            throw new RuntimeException("Failed to read batch", e);
        }
    }

    private void closeReader() {
        try {
            reader.close();
        } catch (Exception e) {
            log.error("Error closing file reader", e);
        }
    }
}
