package ir.yasinzadeh.logprompt.entity;

public class PromptDto {
    private String prompt;
    private String result;

    public String getPrompt() {
        return prompt;
    }

    public PromptDto setPrompt(String prompt) {
        this.prompt = prompt;
        return this;
    }

    public String getResult() {
        return result;
    }

    public PromptDto setResult(String result) {
        this.result = result;
        return this;
    }
}
