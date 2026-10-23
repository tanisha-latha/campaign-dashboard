package com.capstone.dashboard.campaign;

import org.springframework.data.mongodb.repository.MongoRepository;

// Repository: the layer that talks to the database.
// We only write this interface. Spring Data generates the code for us,
// including findAll(), findById(), save(), deleteById() and count().
// <Campaign, String> means "stores Campaign objects whose id is a String".
public interface CampaignRepository extends MongoRepository<Campaign, String> {
}
