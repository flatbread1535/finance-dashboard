package com.financedashboard.transactions;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A REST controller exposing CRUD endpoints for user transactions.
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {

  private final TransactionService transactionService;

  /**
   * Constructs an instance of {@code TransactionController} 
   * with dependency injection.
   *
   * @param transactionService service layer component that handles 
   *     core business logic for transactions
   */
  public TransactionController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  /**
   * Retrieves a single transaction belonging to an authenticated account.
   *
   * @param transactionId the primary key identifier of the transaction to be retrieved
   * @param authentication the currently authenticated account
   * @return a {@code 200 OK} response with a transaction response DTO body
   */
  @GetMapping("/{transactionId}")
  public ResponseEntity<TransactionResponse> getTransaction(
      @PathVariable Long transactionId,
      Authentication authentication
  ) {
    return ResponseEntity
        .ok(transactionService.getTransaction(transactionId, authentication.getName()));
  }

  /**
   * Retrieves all transactions' information with pagination support.
   *
   * @param pageable an object containing page number, page size, and sorting information
   * @param authentication the currently authenticated user
   * @return a {@code 200 OK} response with containing a page of transaction response 
   *     data transfer objects as the body
   */
  @GetMapping
  public ResponseEntity<Page<TransactionResponse>> getAllTransactions(
      Pageable pageable,
      Authentication authentication
  ) {
    return ResponseEntity
        .ok(transactionService.getTransactions(pageable, authentication.getName()));
  }

  /**
   * Creates a new transaction for the authenticated account.
   *
   * @param authentication the currently authenticated account
   * @param request a request DTO with fields of data used to construct a new 
   *     transaction entity
   * @return a {@code 201 Created} response containing the location header of the 
   *     new transaction
   */
  @PostMapping
  public ResponseEntity<Void> createTransaction(
      Authentication authentication,
      @RequestBody @Valid TransactionRequest request
  ) {
    String username = authentication.getName();
    Transaction requestedTransaction = transactionService.createTransaction(username, request);
    URI location = URI.create("/transactions/" + requestedTransaction.getTransactionId());
    return ResponseEntity.created(location).build();
  }

  /**
   * Updates an existing transaction owned by the authenticated account.
   *
   * @param transactionId primary key identifier of the transaction to be updated
   * @param authentication the currently authenticated account
   * @param updateRequest a request DTO with fields of data used to update the transaction
   * @return a {@code 204 No Content} response once the update succeeds
   */
  @PutMapping("/{transactionId}")
  public ResponseEntity<Void> updateTransaction(
      @PathVariable Long transactionId,
      Authentication authentication, 
      @RequestBody @Valid TransactionRequest updateRequest
  ) {
    transactionService.updateTransaction(
        transactionId, 
        authentication.getName(),
        updateRequest
    ); 
    return ResponseEntity.noContent().build();
  }

  /**
   * Deletes a transaction owned by an authenticated account.
   *
   * @param transactionId primary key identifier of the transaction to be deleted
   * @param authentication the currently authenticated account
   * @return a {@code 204 No Content} response once the deletion succeeds
   */
  @DeleteMapping("/{transactionId}")
  public ResponseEntity<Void> deleteTransaction(
      @PathVariable Long transactionId,
      Authentication authentication
  ) {
    transactionService.deleteTransaction(transactionId, authentication.getName());
    return ResponseEntity.noContent().build();
  }
}
