package com.campaignhub.api.campaign;

import com.campaignhub.api.campaign.dto.request.UpdateCampaignCommand;
import com.campaignhub.api.campaign.dto.request.CreateCampaignCommand;
import com.campaignhub.api.campaign.dto.response.CampaignDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface MarketingCampaignMapper {

    CampaignDto toResponse(MarketingCampaign marketingCampaign);

    MarketingCampaign toEntity(UpdateCampaignCommand request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(CreateCampaignCommand request, @MappingTarget MarketingCampaign marketingCampaign);
}