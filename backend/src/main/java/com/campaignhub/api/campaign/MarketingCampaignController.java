package com.campaignhub.api.campaign;

import com.campaignhub.api.campaign.dto.request.UpdateCampaignCommand;
import com.campaignhub.api.campaign.dto.request.CreateCampaignCommand;
import com.campaignhub.api.campaign.dto.response.CampaignDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/campaigns")
@RequiredArgsConstructor
public class MarketingCampaignController {

    private final MarketingCampaignService campaignService;

    @PostMapping
    public ResponseEntity<CampaignDto> createCampaign(
            @Valid @RequestBody UpdateCampaignCommand request) {
        CampaignDto response = campaignService.createCampaign(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CampaignDto> updateCampaign(
            @PathVariable UUID id,
            @Valid @RequestBody CreateCampaignCommand request) {
        CampaignDto response = campaignService.updateCampaign(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampaignDto> getCampaign(@PathVariable UUID id) {
        CampaignDto response = campaignService.getCampaign(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<CampaignDto>> getAllCampaigns(
            @PageableDefault(size = 10, sort = "campaignName") Pageable pageable) {
        Page<CampaignDto> response = campaignService.getAllCampaigns(pageable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable UUID id) {
        campaignService.deleteCampaign(id);
        return ResponseEntity.noContent().build();
    }
}