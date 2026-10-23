package com.capstone.dashboard.campaign;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

// DTO (Data Transfer Object): the JSON body the frontend sends to create or
// edit a campaign. It only has the fields a user may set, so nobody can send
// their own id, raisedAmount or donorCount.
//
// The annotations are validation rules. When the controller uses @Valid,
// Spring checks them and rejects bad input with 400 Bad Request.
public record CampaignRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must be 100 characters or fewer")
        String name,

        @NotBlank(message = "Description is required")
        @Size(max = 500, message = "Description must be 500 characters or fewer")
        String description,

        @Positive(message = "Goal must be greater than 0")
        long goalAmount,

        @NotNull(message = "Status is required")
        CampaignStatus status) {
}
