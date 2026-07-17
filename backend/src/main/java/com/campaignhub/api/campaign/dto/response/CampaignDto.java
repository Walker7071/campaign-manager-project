package com.campaignhub.api.campaign.dto.response;

import com.campaignhub.api.campaign.MarketingCampaignStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CampaignDto(
        UUID id,
        String campaignName,
        List<String> keywords,
        BigDecimal bidAmount,
        BigDecimal campaignFund,
        MarketingCampaignStatus status,
        String town,
        Integer radius
) {}