package com.capstone.dashboard.campaign;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

// Controller: the REST API the frontend calls.
// @RestController means every method's return value is sent back as JSON.
// @RequestMapping puts all these endpoints under /api/campaigns.
@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    private final CampaignService service;

    // Constructor injection again: Spring passes in the CampaignService.
    public CampaignController(CampaignService service) {
        this.service = service;
    }

    // GET /api/campaigns -> list every campaign (view)
    @GetMapping
    public List<Campaign> list() {
        return service.findAll();
    }

    // GET /api/campaigns/stats -> summary numbers for the dashboard
    @GetMapping("/stats")
    public CampaignStats stats() {
        return service.getStats();
    }

    // GET /api/campaigns/{id} -> one campaign; @PathVariable reads {id} from the URL
    @GetMapping("/{id}")
    public Campaign get(@PathVariable String id) {
        return service.findById(id);
    }

    // POST /api/campaigns -> create. @RequestBody reads the JSON body,
    // @Valid checks it against the rules in CampaignRequest.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // reply 201 Created instead of 200
    public Campaign create(@Valid @RequestBody CampaignRequest request) {
        return service.create(request);
    }

    // PUT /api/campaigns/{id} -> edit an existing campaign
    @PutMapping("/{id}")
    public Campaign update(@PathVariable String id, @Valid @RequestBody CampaignRequest request) {
        return service.update(id, request);
    }
}
