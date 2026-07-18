package com.finance_dashboard.goals;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.*;

public record GoalRequest(
    
    @NotBlank(message = "Goal name cannot be blank.")
    @Size(max = 100, message = "Goal name must be no more than 100 characters.")
    String name,
    
    @NotNull(message = "Target amount cannot be null.")
    @Positive(message = "Target amount must be a positive number.")
    BigDecimal targetAmount,
    
    @NotNull(message = "Current amount cannot be null.")
    @PositiveOrZero(message = "Current amount cannot be a negative number.")
    BigDecimal currentAmount,
    
    @NotNull(message = "Target date cannot be null.")
    @FutureOrPresent(message = "Cannot use a past date.")
    LocalDate targetDate,
    
    @NotNull(message = "Goal priority level cannot be null.")
    PriorityLevel priorityLevel,
    
    @NotNull(message = "Goal status cannot be null.")
    Status status,

    @Size(max = 250, message = "Goal description must be no more than 250 characters.")
    String description

) {}
