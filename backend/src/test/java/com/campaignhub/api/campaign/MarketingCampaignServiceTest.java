package com.campaignhub.api.campaign;

import com.campaignhub.api.campaign.dto.request.CreateCampaignCommand;
import com.campaignhub.api.campaign.dto.request.UpdateCampaignCommand;
import com.campaignhub.api.campaign.dto.response.CampaignDto;
import com.campaignhub.api.wallet.EmeraldAccountService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarketingCampaignServiceTest {

    @Mock
    private MarketingCampaignRepository campaignRepository;

    @Mock
    private MarketingCampaignMapper marketingCampaignMapper;

    @Mock
    private EmeraldAccountService accountService;

    @InjectMocks
    private MarketingCampaignService service;

    @Test
    @DisplayName("Powinno pomyślnie utworzyć nową kampanię i pobrać środki")
    void shouldCreateCampaignSuccessfully() {

        CreateCampaignCommand command = new CreateCampaignCommand(
                "Testowa Kampania Letnia",
                List.of("lato", "promocja"),
                new BigDecimal("15.50"),
                new BigDecimal("5000.00"),
                MarketingCampaignStatus.ON,
                "Warszawa",
                25
        );

        MarketingCampaign entityToSave = new MarketingCampaign();
        entityToSave.setCampaignName(command.campaignName());
        entityToSave.setCampaignFund(command.campaignFund());

        MarketingCampaign savedEntity = new MarketingCampaign();
        savedEntity.setId(UUID.randomUUID());
        savedEntity.setCampaignFund(new BigDecimal("5000.00"));

        CampaignDto expectedDto = mock(CampaignDto.class);

        when(marketingCampaignMapper.toEntity(command)).thenReturn(entityToSave);
        when(campaignRepository.save(entityToSave)).thenReturn(savedEntity);
        when(marketingCampaignMapper.toResponse(savedEntity)).thenReturn(expectedDto);

        CampaignDto result = service.createCampaign(command);

        assertNotNull(result, "Zwrócone DTO nie powinno być nullem");

        verify(accountService, times(1)).deductFunds(new BigDecimal("5000.00"));

        verify(campaignRepository, times(1)).save(entityToSave);
    }

    @Test
    @DisplayName("Powinno rzucić wyjątek podczas próby usunięcia nieistniejącej kampanii")
    void shouldThrowExceptionWhenDeletingNonExistentCampaign() {
        UUID nonExistentId = UUID.randomUUID();

        when(campaignRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            service.deleteCampaign(nonExistentId);
        }, "Powinien zostać rzucony EntityNotFoundException w przypadku braku kampanii");

        verify(campaignRepository, never()).delete(any());

        verify(accountService, never()).addFunds(any());
    }

    @Test
    @DisplayName("Powinno zaktualizować kampanię i pobrać z konta tylko różnicę budżetu")
    void shouldUpdateCampaignAndDeductOnlyTheDifference() {
        UUID campaignId = UUID.randomUUID();

        MarketingCampaign existingCampaign = new MarketingCampaign();
        existingCampaign.setId(campaignId);
        existingCampaign.setCampaignName("Stara Kampania");
        existingCampaign.setCampaignFund(new BigDecimal("5000.00"));
        existingCampaign.setBidAmount(new BigDecimal("10.00"));

        UpdateCampaignCommand command = new UpdateCampaignCommand(
                "Nowa Kampania",
                null,
                new BigDecimal("10.00"),
                new BigDecimal("7000.00"),
                MarketingCampaignStatus.ON,
                "Warszawa",
                25
        );

        when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(existingCampaign));
        when(campaignRepository.save(any())).thenReturn(existingCampaign);

        service.updateCampaign(campaignId, command);

        verify(accountService, times(1)).deductFunds(new BigDecimal("2000.00"));
    }

    @Test
    @DisplayName("Powinno rzucić wyjątek, gdy bidAmount jest większy niż campaignFund")
    void shouldThrowExceptionWhenBidIsGreaterThanFund() {
        CreateCampaignCommand command = new CreateCampaignCommand(
                "Błędna Kampania",
                null,
                new BigDecimal("500.00"),
                new BigDecimal("100.00"),
                MarketingCampaignStatus.ON,
                "Warszawa",
                25
        );

        assertThrows(RuntimeException.class, () -> {
            service.createCampaign(command);
        }, "Powinien zostać rzucony wyjątek walidacji funduszy");

        verify(campaignRepository, never()).save(any());
    }
}