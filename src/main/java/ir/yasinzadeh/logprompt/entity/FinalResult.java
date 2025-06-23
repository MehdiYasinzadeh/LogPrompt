package ir.yasinzadeh.logprompt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "FinalResult")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinalResult {
    @Id
    private String id;
    private String log;
    private String mainLog;
    private AiModel aiModel;
    private List<PromptDto> prompts;
}
