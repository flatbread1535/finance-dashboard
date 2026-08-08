package com.financedashboard.goals;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data transfer object representing a response to get a goal.
 *
 * @param goalId the primary key identifier for the goal
 * @param name the name of the goal
 * @param targetAmount the goal's target amount
 * @param currentAmount the current amount saved for the goal
 * @param targetDate the day the goal is desired to be achieved
 * @param priorityLevel the goal's priority to be completed
 * @param status the status of the goal
 * @param description the goal's description
 */
public record GoalResponse(
    Long goalId, 
    String name, 
    BigDecimal targetAmount,
    BigDecimal currentAmount, 
    LocalDate targetDate, 
    PriorityLevel priorityLevel, 
    Status status,
    String description) {
}
