package com.finance_dashboard.budgets;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

public record BudgetRequest(
        
        @NotBlank(message = "Budget name cannot be blank.")
        @Size(max = 100, message = "Budget name must be no more than 100 characters.")
        String name,

        @NotBlank(message = "Budget category cannot be blank.")
        @Size(max = 50, message = "Budget category must be no more than 50 characters.")
        String category,

        @NotNull(message = "Target amount cannot be null.")
        @Positive(message = "Target amount must be a positive number.")
        BigDecimal targetAmount,

        @NotNull(message = "Current spending cannot be null.")
        @PositiveOrZero(message = "Current spending cannot be a negative number.")
        BigDecimal currentSpending,

        @NotNull(message = "Start date cannot be null.")
        @FutureOrPresent(message = "Cannot use a past date.")
        LocalDate startDate,
        
        @NotNull(message = "End date cannot be null.")
        @FutureOrPresent(message = "Cannot use a past date.")
        LocalDate endDate,

        @NotNull(message = "Threshold alert setting cannot be null.")
        Boolean isThresholdAlert,

        @NotNull(message = "Threshold alert value cannot be null.")
        @PositiveOrZero(message = "Threshold alert value cannot be negative.")
        BigDecimal thresholdAlertValue

) {}
