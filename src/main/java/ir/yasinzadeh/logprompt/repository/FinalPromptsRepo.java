package ir.yasinzadeh.logprompt.repository;

import ir.yasinzadeh.logprompt.entity.FinalResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinalPromptsRepo extends MongoRepository<FinalResult,Integer> {
}
