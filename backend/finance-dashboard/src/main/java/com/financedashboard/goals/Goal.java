package com.financedashboard.goals;

import com.financedashboard.accounts.Account;
import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

/**
 * Spring Data JPA entity representing an account's goal.
 */
@Entity
@Table(name = "goals")
public class Goal {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "goal_id", nullable = false)
  private Long goalId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id", nullable = false)
  private Account account;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "target_amount", nullable = false, precision = 15, scale = 2,
      check = @CheckConstraint(constraint = "target_amount >= 0"))
  private BigDecimal targetAmount;

  @Column(name = "current_amount", nullable = false, precision = 15, scale = 2,
      check = @CheckConstraint(constraint = "current_amount >= 0"))
  private BigDecimal currentAmount = BigDecimal.ZERO;

  @CreationTimestamp
  @Column(name = "time_created", nullable = false)
  private LocalDateTime timeCreated;

  @Column(name = "target_date", nullable = false)
  private LocalDate targetDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "priority_level", nullable = false, length = 20)
  private PriorityLevel priorityLevel;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private Status status;

  @Column(name = "description", length = 250)
  private String description;

  protected Goal() {}

  /**
   * Constructor for {@code Budget}.
   *
   * @param goalId the primary key identifier for the goal
   * @param account the account that the goal belongs to
   * @param name the name of the goal
   * @param targetAmount the goal's target amount
   * @param currentAmount the current amount saved for the goal
   * @param timeCreated the time the goal was created
   * @param targetDate the day the goal is desired to be achieved
   * @param priorityLevel the goal's priority to be completed
   * @param status the status of the goal
   * @param description the goal's description
   */
  public Goal(
      Long goalId, 
      Account account, 
      String name, 
      BigDecimal targetAmount,
      BigDecimal currentAmount, 
      LocalDateTime timeCreated, 
      LocalDate targetDate,
      PriorityLevel priorityLevel, Status status, String description) {
    this.goalId = goalId;
    this.account = account;
    this.name = name;
    this.targetAmount = targetAmount;
    this.currentAmount = currentAmount;
    this.timeCreated = timeCreated;
    this.targetDate = targetDate;
    this.priorityLevel = priorityLevel;
    this.status = status;
    this.description = description;
  }

  // Getters and setters

  public Long getGoalId() {
    return goalId;
  }

  public void setGoalId(Long goalId) {
    this.goalId = goalId;
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

  public BigDecimal getTargetAmount() {
    return targetAmount;
  }

  public void setTargetAmount(BigDecimal targetAmount) {
    this.targetAmount = targetAmount;
  }

  public BigDecimal getCurrentAmount() {
    return currentAmount;
  }

  public void setCurrentAmount(BigDecimal currentAmount) {
    this.currentAmount = currentAmount;
  }

  public LocalDateTime getTimeCreated() {
    return timeCreated;
  }

  public void setTimeCreated(LocalDateTime timeCreated) {
    this.timeCreated = timeCreated;
  }

  public LocalDate getTargetDate() {
    return targetDate;
  }

  public void setTargetDate(LocalDate targetDate) {
    this.targetDate = targetDate;
  }  

  public PriorityLevel getPriorityLevel() {
    return priorityLevel;
  }

  public void setPriorityLevel(PriorityLevel priorityLevel) {
    this.priorityLevel = priorityLevel;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
