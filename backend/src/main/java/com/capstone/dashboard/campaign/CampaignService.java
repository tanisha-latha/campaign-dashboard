package com.capstone.dashboard.campaign;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

// Service: the business logic, sitting between the controller (HTTP) and the
// repository (database). @Service registers it with Spring so it can be
// injected wherever it's needed.
@Service
public class CampaignService {

    private final CampaignRepository repository;

    // Constructor injection: Spring sees this constructor and passes in the
    // CampaignRepository for us. No @Autowired needed with a single constructor.
    public CampaignService(CampaignRepository repository) {
        this.repository = repository;
    }

    // All campaigns, newest first.
    public List<Campaign> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public Campaign findById(String id) {
        return repository.findById(id).orElseThrow(() -> new CampaignNotFoundException(id));
    }

    // New campaigns start with nothing raised and no donors.
    public Campaign create(CampaignRequest request) {
        Campaign campaign = new Campaign();
        applyRequest(campaign, request);
        campaign.setRaisedAmount(0);
        campaign.setDonorCount(0);
        campaign.setCreatedAt(Instant.now());
        campaign.setUpdatedAt(campaign.getCreatedAt());
        return repository.save(campaign); // save() inserts and fills in the new id
    }

    // Edit only touches the fields in the request; totals stay as they were.
    public Campaign update(String id, CampaignRequest request) {
        Campaign campaign = findById(id);
        applyRequest(campaign, request);
        campaign.setUpdatedAt(Instant.now());
        return repository.save(campaign); // save() on an existing id updates it
    }

    // Adds up every campaign for the dashboard's summary cards.
    public CampaignStats getStats() {
        List<Campaign> campaigns = repository.findAll();

        int active = 0;
        long raised = 0;
        long goal = 0;
        int donors = 0;
        for (Campaign c : campaigns) {
            if (c.getStatus() == CampaignStatus.ACTIVE) {
                active++;
            }
            raised += c.getRaisedAmount();
            goal += c.getGoalAmount();
            donors += c.getDonorCount();
        }
        int percent = goal == 0 ? 0 : (int) Math.round(raised * 100.0 / goal);

        return new CampaignStats(campaigns.size(), active, raised, goal, donors, percent);
    }

    // Manual mapping: copy the user-editable fields from the DTO onto the document.
    private void applyRequest(Campaign campaign, CampaignRequest request) {
        campaign.setName(request.name().trim());
        campaign.setDescription(request.description().trim());
        campaign.setGoalAmount(request.goalAmount());
        campaign.setStatus(request.status());
    }
}
