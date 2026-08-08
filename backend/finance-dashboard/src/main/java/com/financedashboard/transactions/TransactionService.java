package com.financedashboard.transactions;

import com.financedashboard.accounts.Account;
import com.financedashboard.accounts.AccountRepository;
import com.financedashboard.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service layer for managing account transactions.
 */
@Service
public class TransactionService {

  private final TransactionRepository transactionRepository;
  private final AccountRepository accountRepository;

  /**
   * Creates an instance of {@code TransactionService} with dependency injection.
   *
   * @param transactionRepository repository for persisting and querying transactions
   * @param accountRepository repository for looking up the owning account
   */
  public TransactionService(
      TransactionRepository transactionRepository,
      AccountRepository accountRepository
  ) {
    this.transactionRepository = transactionRepository;
    this.accountRepository = accountRepository;
  }

  /**
   * Retrieves a single transaction, scoped to its owning account's username.
   *
   * @param transactionId id of the transaction to retrieve
   * @param username username of the account that must own the transaction
   * @return a response DTO representing the transaction
   * @throws ResourceNotFoundException if no transaction with that id exists
   *     for the given username
   */
  public TransactionResponse getTransaction(Long transactionId, String username) {
    Transaction transaction = transactionRepository
        .findByTransactionIdAndAccountUsername(transactionId, username)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Could not find transaction."));

    return new TransactionResponse(
        transaction.getTransactionId(),
        transaction.getTimeCreated(), 
        transaction.getAmount(),
        transaction.getCurrency(), 
        transaction.getStatus(),
        transaction.getCategory());
  }

  /**
   * Retrieves a paginated list of transactions belonging to the given username.
   *
   * @param pageable pagination and sorting parameters
   * @param username username of the account whose transactions are returned
   * @return a page of response DTOs representing the account's transactions
   */
  public Page<TransactionResponse> getTransactions(Pageable pageable, String username) {
    return transactionRepository.findByAccountUsername(pageable, username)
        .map(transaction -> new TransactionResponse(
            transaction.getTransactionId(),
            transaction.getTimeCreated(),
            transaction.getAmount(),
            transaction.getCurrency(),
            transaction.getStatus(),
            transaction.getCategory()));
  }

  /**
   * Creates a new transaction under the given username's account.
   *
   * @param username username of the account the transaction will belong to
   * @param request validated transaction fields
   * @return the newly persisted transaction, including its generated id
   * @throws ResourceNotFoundException if no account exists for the given username
   */
  @Transactional
  public Transaction createTransaction(String username, TransactionRequest request) {

    Account account = accountRepository.findByUsername(username).orElseThrow(
        () -> new ResourceNotFoundException("Could not find account."));

    Transaction transaction = new Transaction(
        null, 
        null, 
        account, 
        request.amount(),
        request.currency(), 
        request.status(), request.category());

    return transactionRepository.save(transaction);
  }

  /**
   * Updates the mutable fields of a transaction owned by the given username.
   *
   * @param transactionId id of the transaction to update
   * @param username username of the account that must own the transaction
   * @param updateRequest validated fields to apply to the transaction
   * @throws ResourceNotFoundException if no transaction with that id exists
   *     for the given username
   */
  @Transactional
  public void updateTransaction(
        Long transactionId, 
        String username,        
        TransactionRequest updateRequest
  ) {
    Transaction transaction = transactionRepository
        .findByTransactionIdAndAccountUsername(transactionId, username)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Could not find transaction."));

    transaction.setAmount(updateRequest.amount());
    transaction.setCurrency(updateRequest.currency());
    transaction.setStatus(updateRequest.status());
    transaction.setCategory(updateRequest.category());

    transactionRepository.save(transaction);
  }

  /**
   * Deletes a transaction owned by the given username.
   *
   * @param transactionId id of the transaction to delete
   * @param username username of the account that must own the transaction
   * @throws ResourceNotFoundException if no transaction with that id exists
   *     for the given username
   */
  @Transactional
  public void deleteTransaction(Long transactionId, String username) {
    Transaction transaction = transactionRepository
        .findByTransactionIdAndAccountUsername(transactionId, username)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Could not find transaction."));

    transactionRepository.delete(transaction);
  }
}