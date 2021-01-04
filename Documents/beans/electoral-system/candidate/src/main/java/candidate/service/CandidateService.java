package candidate.service;

import core.entity.Candidate;
import candidate.repository.CandidateRepo;
import core.exception.ApiRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateService {
    @Autowired CandidateRepo candidateRepo;


    /*@Autowired
    public CandidateService(@Qualifier("CandidateRepo") CandidateRepo candidateRepo) {
        this.candidateRepo = candidateRepo;
    }*/

    public Candidate addCandidate(Candidate candidate) {
        if(!candidateRepo.existsByName(candidate.getName()))
            return candidateRepo.save(candidate);
        else
            throw new ApiRequestException("This candidate already exists in the database");
    }

    public Candidate findCandidateByID(String id) {
        return candidateRepo.findById(id).orElseThrow(() ->
                new ApiRequestException("Cannot find candidate with this ID"));
    }

    public Candidate findCandidateByName(String name) {
        return candidateRepo.findByName(name).orElseThrow(() ->
                new ApiRequestException("Cannot find candidate with this ID"));
    }

    public Candidate replaceCandidateByID(String id, Candidate candidate) {
        candidateRepo.findById(id).orElseThrow(() ->
                new ApiRequestException("Cannot find member with this ID"));
        candidate.setId(id);
        candidateRepo.save(candidate);

        return candidate;
    }

    public List<Candidate> findAllCandidates() {
        return (List<Candidate>) candidateRepo.findAll();
    }

    public Candidate removeCandidateByID(String id) {
        Candidate candidate = findCandidateByID(id);
        candidateRepo.deleteById(id);
        return candidate;
    }

    public Candidate removeCandidateByName(String name) {
        Candidate candidate = findCandidateByName(name);
        candidateRepo.deleteByName(name);
        return candidate;
    }

    public void removeAllCandidates() {
        candidateRepo.deleteAll();
    }
}
