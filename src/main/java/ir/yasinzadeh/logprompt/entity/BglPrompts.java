package ir.yasinzadeh.logprompt.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "BglPrompt")
public class BglPrompts {

    @Id
    private int id;
    private String log;
    private List<String> prompts;
    private String result;
    public String response;

    public int getId() {
        return id;
    }

    public BglPrompts setId(int id) {
        this.id = id;
        return this;
    }

    public String getLog() {
        return log;
    }

    public BglPrompts setLog(String log) {
        this.log = log;
        return this;
    }

    public List<String> getPrompts() {
        return prompts;
    }

    public BglPrompts setPrompts(List<String> prompts) {
        this.prompts = prompts;
        return this;
    }

    public String getResult() {
        return result;
    }

    public BglPrompts setResult(String result) {
        this.result = result;
        return this;
    }

    public String getResponse() {
        return response;
    }

    public BglPrompts setResponse(String response) {
        this.response = response;
        return this;
    }
}
