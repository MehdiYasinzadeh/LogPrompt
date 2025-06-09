package ir.yasinzadeh.logprompt.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "FinalPrompts")
public class FinalPrompts {

    @Id
    private String id;
    private String log;
    private List<PromptDto> prompts;

    public String getId() {
        return id;
    }

    public FinalPrompts setId(String id) {
        this.id = id;
        return this;
    }

    public String getLog() {
        return log;
    }

    public FinalPrompts setLog(String log) {
        this.log = log;
        return this;
    }

    public List<PromptDto> getPrompts() {
        return prompts;
    }

    public FinalPrompts setPrompts(List<PromptDto> prompts) {
        this.prompts = prompts;
        return this;
    }
}
