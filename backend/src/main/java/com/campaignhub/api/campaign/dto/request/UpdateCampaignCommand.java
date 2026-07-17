package com.campaignhub.api.campaign.dto.request;

import com.campaignhub.api.campaign.MarketingCampaignStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record UpdateCampaignCommand(
        @NotBlank(message = "Campaign name is mandatory")
        String campaignName,

        @NotEmpty(message = "Keywords are mandatory")
        List<String> keywords,

        @NotNull(message = "Bid amount is mandatory")
        @DecimalMin(value = "0.01", message = "Bid amount must be at least 0.01")
        BigDecimal bidAmount,

        @NotNull(message = "Campaign fund is mandatory")
        @DecimalMin(value = "0.01", message = "Campaign fund must be at least 0.01")
        BigDecimal campaignFund,

        @NotNull(message = "Status is mandatory")
        MarketingCampaignStatus status,

        @NotBlank(message = "Town is mandatory")
        String town,

        @NotNull(message = "Radius is mandatory")
        @Min(value = 1, message = "Radius must be at least 1 km")
        Integer radius
) {}