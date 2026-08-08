package com.financedashboard.transactions;

import com.financedashboard.accounts.Account;
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
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

/**
 * Spring Data JPA entity representing an account's transaction.
 */
@Entity
@Table(name = "transactions")
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long transactionId;

  @CreationTimestamp
  @Column(name = "time_created", nullable = false)
  private LocalDateTime timeCreated;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id", nullable = false)
  private Account account;

  @Column(name = "amount", nullable = false, precision = 15, scale = 2)
  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  @Column(name = "currency", nullable = false, length = 3)
  private Currency currency;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private Status status;

  @Column(name = "category", nullable = false, length = 50)
  private String category;

  protected Transaction() {}

  /**
   * Constructor for {@code Transaction}.
   *
   * @param transactionId primary key for the transaction
   * @param timeCreated time the transaction was created
   * @param account the account the transaction belongs to
   * @param amount the transaction's amount
   * @param currency the currency used in the transaction
   * @param status the status of the transaction
   * @param category the category of the transaction
   */
  public Transaction(
      Long transactionId, 
      LocalDateTime timeCreated, 
      Account account,
      BigDecimal amount, 
      Currency currency, 
      Status status, 
      String category
  ) {
    this.transactionId = transactionId;
    this.timeCreated = timeCreated;
    this.account = account;
    this.amount = amount;
    this.currency = currency;
    this.status = status;
    this.category = category;
  }

  // Getters and setters

  public Long getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(Long transactionId) {
    this.transactionId = transactionId;
  }

  public LocalDateTime getTimeCreated() {
    return timeCreated;
  }

  public void setTimeCreated(LocalDateTime timeCreated) {
    this.timeCreated = timeCreated;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public Currency getCurrency() {
    return currency;
  }

  public void setCurrency(Currency currency) {
    this.currency = currency;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }
}
