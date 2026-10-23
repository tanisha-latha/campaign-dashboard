package com.capstone.dashboard.campaign;

// The only values a campaign's status can have.
// Stored in MongoDB and sent in JSON as text: "ACTIVE", "PAUSED" or "ENDED".
public enum CampaignStatus {
    ACTIVE,
    PAUSED,
    ENDED
}
