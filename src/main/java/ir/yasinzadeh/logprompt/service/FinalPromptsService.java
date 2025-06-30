package ir.yasinzadeh.logprompt.service;

import ir.yasinzadeh.logprompt.entity.FinalPrompts;
import ir.yasinzadeh.logprompt.repository.FinalPromptsRepo;
import org.springframework.stereotype.Service;

@Service
public class FinalPromptsService {
    private final FinalPromptsRepo repository;

    public FinalPromptsService(FinalPromptsRepo repository) {
        this.repository = repository;
    }

    public void save(FinalPrompts prompts) {
        repository.save(prompts);
    }
}
