package candidate.controller;

import core.entity.Candidate;
import candidate.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/candidate")
public class CandidateController {

    @Autowired CandidateService candidateService;

   /* @Autowired
    public CandidateController(@Qualifier("CandidateService") CandidateService candidateService) {
        this.candidateService = candidateService;
    }*/

    @PostMapping
    public Candidate postCandidate(@RequestBody Candidate candidate) {
        return candidateService.addCandidate(candidate);
    }

    @GetMapping
    public List<Candidate> getAllCandidates(){
        return candidateService.findAllCandidates();
    }

    @GetMapping("/id/{id}")
    public Candidate getCandidateByID(@PathVariable("id") String id){
        return candidateService.findCandidateByID(id);
    }

    @GetMapping("/name/")
    public Candidate getCandidateByName(@PathVariable("name") String name){
        return candidateService.findCandidateByName(name);
    }

    @PutMapping("/id/{id}")
    public Candidate putCandidateByID(@PathVariable("id") String id, @RequestBody Candidate candidate) {
        return candidateService.replaceCandidateByID(id, candidate);
    }

    @DeleteMapping
    public void deleteAllCandidates() {
        candidateService.removeAllCandidates();
    }

    @DeleteMapping("/id/{id}")
    public Candidate deleteCandidateByID(@PathVariable("id") String id) {
        return candidateService.removeCandidateByID(id);
    }

    @DeleteMapping("/name/{name}")
    public Candidate deleteCandidateByName(@PathVariable("name") String name) {
        return candidateService.removeCandidateByName(name);
    }
}
