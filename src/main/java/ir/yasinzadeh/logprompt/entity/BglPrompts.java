package ir.yasinzadeh.logprompt.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "BglPrompt")
public class BglPrompts {

    @Id
    private int id;
    private String log;
    private List<PromptDto> prompts;

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

    public List<PromptDto> getPrompts() {
        return prompts;
    }

    public BglPrompts setPrompts(List<PromptDto> prompts) {
        this.prompts = prompts;
        return this;
    }
}
