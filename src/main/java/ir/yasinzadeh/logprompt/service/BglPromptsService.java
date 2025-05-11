package ir.yasinzadeh.logprompt.service;

import ir.yasinzadeh.logprompt.entity.BglPrompts;
import ir.yasinzadeh.logprompt.repository.BglPromptsRepo;
import org.springframework.stereotype.Service;

@Service
public class BglPromptsService {
    private final BglPromptsRepo repository;

    public BglPromptsService(BglPromptsRepo repository) {
        this.repository = repository;
    }

    public void save(BglPrompts prompts) {
        repository.save(prompts);
    }
}
