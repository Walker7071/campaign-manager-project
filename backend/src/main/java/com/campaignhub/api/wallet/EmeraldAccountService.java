package com.campaignhub.api.wallet;

import com.campaignhub.exception.InsufficientFundsException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class EmeraldAccountService {

    private BigDecimal balance = new BigDecimal("10000.00");

    public BigDecimal getBalance() {
        return balance;
    }

    public void deductFunds(BigDecimal amount) {
        if (amount.compareTo(balance) > 0) {
            throw new InsufficientFundsException("Insufficient funds in Emerald account.");
        }
        balance = balance.subtract(amount);
    }

    public void addFunds(BigDecimal amount) {
        balance = balance.add(amount);
    }
}