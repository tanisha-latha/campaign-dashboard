package com.capstone.dashboard.campaign;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Unit tests for CampaignService (JUnit 5 + Mockito).
// The repository is a Mockito "mock", a fake we control, so these tests run
// without MongoDB and only check the service's own logic.
class CampaignServiceTest {

    private CampaignRepository repository;
    private CampaignService service;

    @BeforeEach
    void setUp() {
        repository = mock(CampaignRepository.class);
        service = new CampaignService(repository);
        // save() returns whatever it was given, like the real repository would.
        when(repository.save(any(Campaign.class))).thenAnswer(call -> call.getArgument(0));
    }

    @Test
    void createStartsWithNothingRaised() {
        Campaign created = service.create(
                new CampaignRequest("  Book Drive ", "Books for schools", 1_000, CampaignStatus.ACTIVE));

        assertThat(created.getName()).isEqualTo("Book Drive"); // trimmed
        assertThat(created.getRaisedAmount()).isZero();
        assertThat(created.getDonorCount()).isZero();
        assertThat(created.getCreatedAt()).isNotNull();
    }

    @Test
    void updateKeepsTotals() {
        Campaign existing = campaign(500, 1_000, 7, CampaignStatus.ACTIVE);
        when(repository.findById("abc")).thenReturn(Optional.of(existing));

        Campaign updated = service.update("abc",
                new CampaignRequest("New name", "New description", 2_000, CampaignStatus.PAUSED));

        assertThat(updated.getName()).isEqualTo("New name");
        assertThat(updated.getGoalAmount()).isEqualTo(2_000);
        assertThat(updated.getStatus()).isEqualTo(CampaignStatus.PAUSED);
        assertThat(updated.getRaisedAmount()).isEqualTo(500); // unchanged
        assertThat(updated.getDonorCount()).isEqualTo(7); // unchanged
    }

    @Test
    void updateUnknownIdThrowsNotFound() {
        when(repository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update("missing",
                new CampaignRequest("x", "y", 1, CampaignStatus.ACTIVE)))
                .isInstanceOf(CampaignNotFoundException.class);
    }

    @Test
    void statsAddUpAllCampaigns() {
        when(repository.findAll()).thenReturn(List.of(
                campaign(250, 1_000, 5, CampaignStatus.ACTIVE),
                campaign(750, 1_000, 10, CampaignStatus.ENDED)));

        CampaignStats stats = service.getStats();

        assertThat(stats.totalCampaigns()).isEqualTo(2);
        assertThat(stats.activeCampaigns()).isEqualTo(1);
        assertThat(stats.totalRaised()).isEqualTo(1_000);
        assertThat(stats.totalGoal()).isEqualTo(2_000);
        assertThat(stats.totalDonors()).isEqualTo(15);
        assertThat(stats.percentOfGoal()).isEqualTo(50);
    }

    private Campaign campaign(long raised, long goal, int donors, CampaignStatus status) {
        Campaign c = new Campaign();
        c.setName("Test");
        c.setDescription("Test");
        c.setRaisedAmount(raised);
        c.setGoalAmount(goal);
        c.setDonorCount(donors);
        c.setStatus(status);
        return c;
    }
}
