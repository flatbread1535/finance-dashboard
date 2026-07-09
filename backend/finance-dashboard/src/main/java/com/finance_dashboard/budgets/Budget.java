package com.finance_dashboard.budgets;

import com.finance_dashboard.accounts.Account;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "budgets")
public class Budget {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "budget_id")
    private Long budgetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(
        name = "target_amount", 
        nullable = false, 
        precision = 15, 
        scale = 2, 
        check = @CheckConstraint(constraint = "target_amount >= 0")
    )
    private BigDecimal targetAmount;

    @Column(
        name = "current_spending", 
        nullable = false, 
        precision = 15, 
        scale = 2, 
        check = @CheckConstraint(constraint = "current_spending >= 0")
    )
    private BigDecimal currentSpending = BigDecimal.ZERO;

    @Column(name = "time_created", nullable = false)
    private LocalDateTime timeCreated;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "is_threshold_alert", nullable = false)
    private Boolean isThresholdAlert = false;

    @Column(
        name = "threshold_alert_value", 
        nullable = false,
        precision = 15, 
        scale = 2, 
        check = @CheckConstraint(constraint = "threshold_alert_value >= 0")
    )
    private BigDecimal thresholdAlertValue;

    @PrePersist
    @PreUpdate
    private void checkIfBudgetDatesAreValid() {
        if (startDate != null && endDate != null && !startDate.isBefore(endDate)) {
            throw new IllegalArgumentException(
                "Budget dates are invalid. The start_date " + startDate + " must be before end_date " + endDate + "."
            );
        }
    }

    protected Budget() {}

    public Budget(
        Long budgetId,
        Account account,
        String name,
        String category,
        BigDecimal targetAmount,
        BigDecimal currentSpending,
        LocalDateTime timeCreated,
        LocalDate startDate,
        LocalDate endDate,
        Boolean isThresholdAlert,
        BigDecimal thresholdAlertValue
    ) {
        this.budgetId = budgetId;
        this.account = account;
        this.name = name;
        this.category = category;
        this.targetAmount = targetAmount;
        this.currentSpending = currentSpending;
        this.timeCreated = timeCreated;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isThresholdAlert = isThresholdAlert;
        this.thresholdAlertValue = thresholdAlertValue;
    }

    public Long getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(Long budgetId) {
        this.budgetId = budgetId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
    }

    public BigDecimal getCurrentSpending() {
        return currentSpending;
    }

    public void setCurrentSpending(BigDecimal currentSpending) {
        this.currentSpending = currentSpending;
    }

    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(LocalDateTime timeCreated) {
        this.timeCreated = timeCreated;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsThresholdAlert() {
        return isThresholdAlert;
    }

    public void setIsThresholdAlert(Boolean isThresholdAlert) {
        this.isThresholdAlert = isThresholdAlert;
    }

    public BigDecimal getThresholdAlertValue() {
        return thresholdAlertValue;
    }

    public void setThresholdAlertValue(BigDecimal thresholdAlertValue) {
        this.thresholdAlertValue = thresholdAlertValue;
    }
}
