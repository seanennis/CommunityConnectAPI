package communityconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * API written to connect the Community Connect android application to its database.
 * To be hosted on an EC2 instance along side a mongo database.
 * @author Sean Ennis O'Toole
 */

@SpringBootApplication
@EnableScheduling
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

