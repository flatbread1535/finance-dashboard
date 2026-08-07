package com.financedashboard.accounts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Account REST controller responsible letting admins manage accounts.
 */
@RestController
@RequestMapping("/admin/accounts")
@PreAuthorize("hasRole('ADMIN')")
public class AdminAccountController {
  private AccountService accountService;

  /**
   * Constructs an instance of {@code AdminAccountController} with dependency injection.
   *
   * @param accountService service layer component that handles core business logic for accounts
   */
  public AdminAccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  /**
   * Retrieves a user's account information.
   *
   * @param accountId the primary key identifier of the account to be retrieved
   * @return a {@code 200 OK} response containing an account response DTO body
   */
  @GetMapping("/{accountId}")
  public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long accountId) {
    return ResponseEntity.ok(accountService.getAccountById(accountId));
  }

  /**
   * Retrieves all accounts' information with pagination support.
   *
   * @param pageable an object containing page number, page size, and sorting information
   * @return a {@code 200 OK} response containing a page of account response 
   *     data transfer objects as the body
   */
  @GetMapping
  public ResponseEntity<Page<AccountResponse>> getAllAccounts(Pageable pageable) {
    return ResponseEntity.ok(accountService.getAllAccounts(pageable));
  }

  /**
   * Deletes a user's account.
   *
   * @param accountId the primary key identifier of the account to be retrieved
   * @return a {@code 204 No Content} response if the account is deleted
   */
  @DeleteMapping("/{accountId}")
  public ResponseEntity<Void> deleteAccountById(@PathVariable Long accountId) {
    accountService.deleteAccountById(accountId);
    return ResponseEntity.noContent().build();
  }
}
