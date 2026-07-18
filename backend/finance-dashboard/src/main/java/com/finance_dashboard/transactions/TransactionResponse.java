package com.finance_dashboard.transactions;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long transactionId,
        LocalDateTime timeCreated,
        BigDecimal amount,
        Currency currency,
        Status status,
        String category) {
}
