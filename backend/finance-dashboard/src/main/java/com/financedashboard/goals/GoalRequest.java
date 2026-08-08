package com.financedashboard.goals;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data transfer object representing a request to create a new goal.
 *
 * @param name the name of the goal
 * @param targetAmount the goal's target amount
 * @param currentAmount the current amount saved for the goal
 * @param targetDate the day the goal is desired to be achieved
 * @param priorityLevel the goal's priority to be completed
 * @param status the status of the goal
 * @param description the goal's description
 */
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
