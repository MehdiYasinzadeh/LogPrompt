package ir.yasinzadeh.logprompt.entity;

public class PromptDto {
    private String prompt;
    private String result;
    private AiModel aiModel;

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

    public AiModel getAiModel() {
        return aiModel;
    }

    public PromptDto setAiModel(AiModel aiModel) {
        this.aiModel = aiModel;
        return this;
    }
}
