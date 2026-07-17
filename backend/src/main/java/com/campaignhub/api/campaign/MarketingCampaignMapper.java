package com.campaignhub.api.campaign;

import com.campaignhub.api.campaign.dto.request.CreateCampaignCommand;
import com.campaignhub.api.campaign.dto.request.UpdateCampaignCommand;
import com.campaignhub.api.campaign.dto.response.CampaignDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MarketingCampaignMapper {

    CampaignDto toResponse(MarketingCampaign entity);

    MarketingCampaign toEntity(CreateCampaignCommand request);

    void updateEntityFromRequest(UpdateCampaignCommand request, @MappingTarget MarketingCampaign entity);
}