package com.capstone.dashboard.campaign;

// DTO for the dashboard's summary numbers (GET /api/campaigns/stats).
// A record is a short way to write a class that just holds data.
public record CampaignStats(
        int totalCampaigns,
        int activeCampaigns,
        long totalRaised,
        long totalGoal,
        int totalDonors,
        int percentOfGoal) {
}
