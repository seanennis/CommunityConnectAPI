package candidate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import core.entity.Candidate;

@SpringBootApplication
@EnableEurekaClient
public class CandidateApplication {
    public static void main(String[] args) {
        SpringApplication.run(CandidateApplication.class, args);
    }

    public static final Candidate[] candidates = {
            new Candidate("Sean Ennis O'Toole", "PartyA", "Blah Blah Blah PartyA Blah Blah Blah"),
            new Candidate("Adam Shorten", "The Adam Party", "Will strengthen the Adam population"),
            new Candidate("Luke Murphy", "PartyB", "Blah Blah Blah PartyB Blah Blah Blah"),
            new Candidate("Adam Waldron", "The Adam Party", "Will encourage cat owners to consider the name Adam for their pets"),
            new Candidate("Ronan Kelly", "PartyC", "Blah Blah Blah PartyC Blah Blah Blah")
    };
}
