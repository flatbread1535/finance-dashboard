package com.finance_dashboard.transactions;

import com.finance_dashboard.accounts.Account;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Column(name = "time_created", nullable = false)
    private LocalDateTime timeCreated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "category", nullable = true)
    private String category;

    protected Transaction() {}
    
    public Transaction(
        Long transactionId,
        LocalDateTime timeCreated,
        Account account,
        BigDecimal amount,
        String currency,
        String status,
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
}
