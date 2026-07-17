package com.campaignhub.api.campaign;

import com.campaignhub.api.campaign.dto.request.CreateCampaignCommand;
import com.campaignhub.api.campaign.dto.request.UpdateCampaignCommand;
import com.campaignhub.api.campaign.dto.response.CampaignDto;
import com.campaignhub.exception.InvalidCampaignException;
import com.campaignhub.api.wallet.EmeraldAccountService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MarketingCampaignService {

    private final MarketingCampaignRepository campaignRepository;
    private final MarketingCampaignMapper marketingCampaignMapper;
    private final EmeraldAccountService accountService;

    @Transactional
    public CampaignDto createCampaign(UpdateCampaignCommand request) {
        validateBidAgainstFund(request.bidAmount(), request.campaignFund());

        MarketingCampaign marketingCampaign = marketingCampaignMapper.toEntity(request);
        accountService.deductFunds(marketingCampaign.getCampaignFund());

        return marketingCampaignMapper.toResponse(campaignRepository.save(marketingCampaign));
    }

    @Transactional
    public CampaignDto updateCampaign(UUID id, CreateCampaignCommand request) {
        MarketingCampaign marketingCampaign = getCampaignById(id);
        BigDecimal oldFund = marketingCampaign.getCampaignFund();

        marketingCampaignMapper.updateEntityFromRequest(request, marketingCampaign);

        validateBidAgainstFund(marketingCampaign.getBidAmount(), marketingCampaign.getCampaignFund());

        handleFundUpdate(oldFund, marketingCampaign.getCampaignFund());

        return marketingCampaignMapper.toResponse(campaignRepository.save(marketingCampaign));
    }

    @Transactional
    public void deleteCampaign(UUID id) {
        MarketingCampaign marketingCampaign = getCampaignById(id);

        accountService.addFunds(marketingCampaign.getCampaignFund());

        campaignRepository.delete(marketingCampaign);
    }

    @Transactional(readOnly = true)
    public CampaignDto getCampaign(UUID id) {
        return marketingCampaignMapper.toResponse(getCampaignById(id));
    }

    @Transactional(readOnly = true)
    public Page<CampaignDto> getAllCampaigns(Pageable pageable) {
        return campaignRepository.findAll(pageable)
                .map(marketingCampaignMapper::toResponse);
    }

    private MarketingCampaign getCampaignById(UUID id) {
        return campaignRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Campaign with given ID not found"));
    }

    private void validateBidAgainstFund(BigDecimal bidAmount, BigDecimal campaignFund) {
        if (bidAmount.compareTo(campaignFund) > 0) {
            throw new InvalidCampaignException("Bid amount cannot be greater than campaign fund");
        }
    }

    private void handleFundUpdate(BigDecimal oldFund, BigDecimal newFund) {
        int comparison = newFund.compareTo(oldFund);

        if (comparison > 0) {
            BigDecimal diff = newFund.subtract(oldFund);
            accountService.deductFunds(diff);
        } else if (comparison < 0) {
            BigDecimal diff = oldFund.subtract(newFund);
            accountService.addFunds(diff);
        }
    }
}