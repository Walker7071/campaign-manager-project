package com.campaignhub.api.campaign.dto.request;

import com.campaignhub.api.campaign.MarketingCampaignStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.util.List;

public record CreateCampaignCommand(
        String campaignName,

        List<String> keywords,

        @DecimalMin(value = "0.01", message = "Bid amount must be at least 0.01")
        BigDecimal bidAmount,

        @DecimalMin(value = "0.01", message = "Campaign fund must be at least 0.01")
        BigDecimal campaignFund,

        MarketingCampaignStatus status,

        String town,

        @Min(value = 1, message = "Radius must be at least 1 km")
        Integer radius
) {}