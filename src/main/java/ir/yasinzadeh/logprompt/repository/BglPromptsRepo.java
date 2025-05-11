package ir.yasinzadeh.logprompt.repository;

import ir.yasinzadeh.logprompt.entity.BglPrompts;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BglPromptsRepo extends MongoRepository<BglPrompts,Integer> {
}
