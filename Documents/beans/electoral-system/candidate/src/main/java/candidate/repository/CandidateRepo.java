package candidate.repository;

import core.entity.Candidate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateRepo extends CrudRepository<Candidate, String> {
    Optional<Candidate> findByName(String name);

    boolean existsByName(String Name);

    void deleteByName(String name);
}