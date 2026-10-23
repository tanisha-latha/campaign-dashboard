package com.capstone.dashboard.campaign;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// One campaign as it is stored in MongoDB.
// @Document says "save objects of this class in the 'campaigns' collection".
// Each object becomes one MongoDB document (a JSON-like record).
@Document("campaigns")
public class Campaign {

    // @Id marks the primary key. MongoDB generates it when we save a new campaign.
    @Id
    private String id;

    private String name;
    private String description;
    private long goalAmount; // target in whole dollars
    private long raisedAmount; // donated so far, in whole dollars
    private int donorCount;
    private CampaignStatus status;
    private Instant createdAt;
    private Instant updatedAt;

    // Spring Data needs an empty constructor to build objects from the database.
    public Campaign() {
    }

    // Getters and setters: Spring reads and writes fields through these,
    // both when talking to MongoDB and when turning objects into JSON.

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getGoalAmount() {
        return goalAmount;
    }

    public void setGoalAmount(long goalAmount) {
        this.goalAmount = goalAmount;
    }

    public long getRaisedAmount() {
        return raisedAmount;
    }

    public void setRaisedAmount(long raisedAmount) {
        this.raisedAmount = raisedAmount;
    }

    public int getDonorCount() {
        return donorCount;
    }

    public void setDonorCount(int donorCount) {
        this.donorCount = donorCount;
    }

    public CampaignStatus getStatus() {
        return status;
    }

    public void setStatus(CampaignStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
