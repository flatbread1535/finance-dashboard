package com.finance_dashboard.budgets;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record BudgetResponse(
    Long budgetId,
    String name,
    String category,
    BigDecimal targetAmount,
    BigDecimal currentSpending,
    LocalDateTime timeCreated,
    LocalDate startDate,
    LocalDate endDate,
    Boolean isThresholdAlert,
    BigDecimal thresholdAlertValue
) {}
