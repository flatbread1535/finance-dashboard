package com.finance_dashboard.transactions;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountUsername(String username);
    Optional<Transaction> findByTransactionIdAndAccountUsername(Long transactionId, String username);
}
