package ir.yasinzadeh.logprompt.service;

import ir.yasinzadeh.logprompt.entity.FinalResult;
import ir.yasinzadeh.logprompt.repository.FinalPromptsRepo;
import org.springframework.stereotype.Service;

@Service
public class FinalPromptsService {
    private final FinalPromptsRepo repository;

    public FinalPromptsService(FinalPromptsRepo repository) {
        this.repository = repository;
    }

    public void save(FinalResult prompts) {
        repository.save(prompts);
    }
}
