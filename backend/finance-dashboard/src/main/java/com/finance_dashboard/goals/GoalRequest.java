package com.finance_dashboard.goals;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GoalRequest(
    Long accountId,
    String name,
    BigDecimal targetAmount,
    BigDecimal currentAmount,
    LocalDate targetDate,
    String priorityLevel,
    String status,
    String description
) {}
