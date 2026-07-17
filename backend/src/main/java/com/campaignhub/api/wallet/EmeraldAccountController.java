package com.campaignhub.api.wallet;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class EmeraldAccountController {

    private final EmeraldAccountService accountService;

    @GetMapping("/balance")
    public ResponseEntity<AccountBalanceResponse> getBalance() {
        BigDecimal currentBalance = accountService.getBalance();
        return ResponseEntity.ok(new AccountBalanceResponse(currentBalance));
    }

    public record AccountBalanceResponse(BigDecimal balance) {}
}