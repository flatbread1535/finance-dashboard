package com.finance_dashboard.transactions;

import com.finance_dashboard.accounts.Account;
import com.finance_dashboard.accounts.AccountRepository;
import com.finance_dashboard.accounts.AccountNotFoundException;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(
        TransactionRepository transactionRepository, 
        AccountRepository accountRepository
    ) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public Optional<Transaction> getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId);
    }

    public Iterable<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Transactional
    public Transaction createTransaction(TransactionRequest request) {

        Account account = accountRepository.findById(request.accountId())
                .orElseThrow(() -> new AccountNotFoundException("Could not find account."));

        Transaction newTransaction = new Transaction(
            null, 
            LocalDateTime.now(),
            account,
            request.amount(),
            request.currency(),
            request.status(),
            request.category()
        );

        return transactionRepository.save(newTransaction);
    }

    @Transactional
    public void updateTransaction(Long transactionId, TransactionRequest updateRequest) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Could not find transaction."));

        transaction.setAmount(updateRequest.amount());
        transaction.setCurrency(updateRequest.currency());
        transaction.setStatus(updateRequest.status());
        transaction.setCategory(updateRequest.category());
        
        transactionRepository.save(transaction);
    }

    @Transactional
    public void deleteTransaction(Long transactionId) {
        if (!transactionRepository.existsById(transactionId)) {
            throw new TransactionNotFoundException("Could not find transaction.");
        }

        transactionRepository.deleteById(transactionId);
    }
}
