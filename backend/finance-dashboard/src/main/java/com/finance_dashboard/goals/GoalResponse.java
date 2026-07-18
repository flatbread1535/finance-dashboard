package com.finance_dashboard.goals;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GoalResponse(
    Long goalId,
    String name,
    BigDecimal targetAmount,
    BigDecimal currentAmount,
    LocalDate targetDate,
    PriorityLevel priorityLevel,
    Status status, 
    String description
) {}
