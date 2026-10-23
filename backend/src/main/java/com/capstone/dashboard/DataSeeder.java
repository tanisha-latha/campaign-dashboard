package com.capstone.dashboard;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.capstone.dashboard.campaign.Campaign;
import com.capstone.dashboard.campaign.CampaignRepository;
import com.capstone.dashboard.campaign.CampaignStatus;

// Puts sample campaigns into MongoDB the first time the app starts, so the
// dashboard has something to show. CommandLineRunner.run() is called once,
// right after Spring Boot finishes starting.
@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final CampaignRepository repository;

    public DataSeeder(CampaignRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        // Only seed an empty database, so your own edits aren't overwritten.
        if (repository.count() > 0) {
            return;
        }

        repository.saveAll(List.of(
                sample("Paws & Whiskers Shelter", "Food, vet care and new homes for rescued cats and dogs.",
                        20_000, 12_450, 184, CampaignStatus.ACTIVE, 40),
                sample("Wildfire Relief Fund", "Emergency supplies and shelter for families displaced by wildfires.",
                        50_000, 47_800, 612, CampaignStatus.ACTIVE, 30),
                sample("Youth Mentorship Program", "Pairing teens with volunteer mentors for tutoring and career advice.",
                        15_000, 3_200, 41, CampaignStatus.PAUSED, 20),
                sample("Community Garden Project", "Turning empty lots into gardens that grow fresh produce for local food banks.",
                        8_000, 8_000, 97, CampaignStatus.ENDED, 10)));

        log.info("Seeded {} sample campaigns", repository.count());
    }

    private Campaign sample(String name, String description, long goal, long raised, int donors,
            CampaignStatus status, int daysAgo) {
        Campaign c = new Campaign();
        c.setName(name);
        c.setDescription(description);
        c.setGoalAmount(goal);
        c.setRaisedAmount(raised);
        c.setDonorCount(donors);
        c.setStatus(status);
        c.setCreatedAt(Instant.now().minus(daysAgo, ChronoUnit.DAYS));
        c.setUpdatedAt(c.getCreatedAt());
        return c;
    }
}
