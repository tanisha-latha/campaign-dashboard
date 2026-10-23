package com.capstone.dashboard.campaign;

// Custom exception thrown when someone asks for a campaign id that doesn't exist.
// GlobalExceptionHandler turns it into a 404 Not Found response.
public class CampaignNotFoundException extends RuntimeException {

    public CampaignNotFoundException(String id) {
        super("Campaign not found: " + id);
    }
}
