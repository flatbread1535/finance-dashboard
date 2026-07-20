package com.finance_dashboard.transactions;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByAccountUsername(Pageable pageable, String username);
    Optional<Transaction> findByTransactionIdAndAccountUsername(Long transactionId, String username);
}
