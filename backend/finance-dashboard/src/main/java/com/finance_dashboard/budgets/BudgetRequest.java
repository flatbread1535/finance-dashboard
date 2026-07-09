package com.finance_dashboard.budgets;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BudgetRequest(
        Long accountId,
        String name,
        String category,
        BigDecimal targetAmount,
        BigDecimal currentSpending,
        LocalDate startDate,
        LocalDate endDate,
        Boolean isThresholdAlert,
        BigDecimal thresholdAlertValue
) {}
