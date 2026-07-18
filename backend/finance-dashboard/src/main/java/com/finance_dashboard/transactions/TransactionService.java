package com.finance_dashboard.transactions;

import com.finance_dashboard.ResourceNotFoundException;
import com.finance_dashboard.accounts.Account;
import com.finance_dashboard.accounts.AccountRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public TransactionResponse getTransaction(Long transactionId, String username) {
        Transaction transaction = transactionRepository.findByTransactionIdAndAccountUsername(transactionId, username)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find transaction."));

        return new TransactionResponse(
                transaction.getTransactionId(),
                transaction.getTimeCreated(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getStatus(),
                transaction.getCategory());
    }

    public List<TransactionResponse> getTransactions(String username) {
        return transactionRepository.findByAccountUsername(username)
                .stream()
                .map(transaction -> new TransactionResponse(
                        transaction.getTransactionId(),
                        transaction.getTimeCreated(),
                        transaction.getAmount(),
                        transaction.getCurrency(),
                        transaction.getStatus(),
                        transaction.getCategory()))
                .toList();
    }

    @Transactional
    public Transaction createTransaction(String username, TransactionRequest request) {

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find account."));

        Transaction transaction = new Transaction(
                null,
                null,
                account,
                request.amount(),
                request.currency(),
                request.status(),
                request.category());

        return transactionRepository.save(transaction);
    }

    @Transactional
    public void updateTransaction(Long transactionId, String username, TransactionRequest updateRequest) {
        Transaction transaction = transactionRepository.findByTransactionIdAndAccountUsername(transactionId, username)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find transaction."));

        transaction.setAmount(updateRequest.amount());
        transaction.setCurrency(updateRequest.currency());
        transaction.setStatus(updateRequest.status());
        transaction.setCategory(updateRequest.category());

        transactionRepository.save(transaction);
    }

    @Transactional
    public void deleteTransaction(Long transactionId, String username) {
        Transaction transaction = transactionRepository.findByTransactionIdAndAccountUsername(transactionId, username)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find transaction."));

        transactionRepository.delete(transaction);
    }
}
