package com.financedashboard.budgets;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data transfer object representing a response to get a transaction.
 *
 * @param budgetId the primary key identifier for the budget
 * @param name the name of the budget
 * @param category the category of the budget
 * @param targetAmount the amount expected to be spent
 * @param currentSpending the current amount spent
 * @param timeCreated the time the budget was created
 * @param startDate the day the budget period began
 * @param endDate the day the budget period ends
 * @param isThresholdAlert if an alert should be sent if threshold value is reached
 * @param thresholdAlertValue the treshold value that, when reached, sends an alert
 */
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
