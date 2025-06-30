package ir.yasinzadeh.logprompt.repository;

import ir.yasinzadeh.logprompt.entity.FinalPrompts;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinalPromptsRepo extends MongoRepository<FinalPrompts,Integer> {
}
