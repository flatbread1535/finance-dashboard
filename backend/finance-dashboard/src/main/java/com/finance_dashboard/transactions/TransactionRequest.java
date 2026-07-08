package com.finance_dashboard.transactions;

import java.math.BigDecimal;

public record TransactionRequest(
    Long accountId,
    BigDecimal amount,
    String currency,
    String status,
    String category
) {}
