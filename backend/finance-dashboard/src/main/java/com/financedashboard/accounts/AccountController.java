package com.financedashboard.accounts;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Account REST controller responsible for account management.
 */
@RestController
@RequestMapping("/accounts")
public class AccountController {
  private final AccountService accountService;

  /**
   * Constructs an instance of {@code AccountController} with dependency injection.
   *
   * @param accountService service layer component that handles core business logic for accounts
   */
  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  /**
   * Retrieves an authenticated user's account information.
   *
   * @param authentication the current authenticated user
   * @return a {@code 200 OK} response containing an account response DTO body
   */
  @GetMapping("/me")
  public ResponseEntity<AccountResponse> getAccount(Authentication authentication) {
    String username = authentication.getName();
    return ResponseEntity.ok(accountService.getAccountByUsername(username));
  }

  /**
   * Creates a new account.
   *
   * @param accountRequest an account creation data transfer object with information
   *     to create an account
   * @param ucb utility for constructing the URI of the newly created account
   * @return a {@code 201 Created} response containing the location header of the 
   *     new account
   */
  @PostMapping
  public ResponseEntity<Void> createAccount(
        @RequestBody @Valid AccountCreateRequest accountRequest,
        UriComponentsBuilder ucb
  ) {
    Account savedAccount = accountService.createAccount(accountRequest);
    URI locationOfNewAccount = ucb.path("/accounts/{accountId}")
        .buildAndExpand(savedAccount.getAccountId()).toUri();
    return ResponseEntity.created(locationOfNewAccount).build();
  }

  /**
   * Updates the authenticated user's account information.
   *
   * @param authentication the current authenticated user
   * @param updateRequest an account update request data transfer object with information
   *     to update an account
   * @return a {@code 204 No Content} response if the update succeeds
   */
  @PutMapping("/me")
  public ResponseEntity<Void> updateAccount(Authentication authentication,
      @RequestBody @Valid AccountUpdateRequest updateRequest) {
    accountService.updateAccount(authentication.getName(), updateRequest);
    return ResponseEntity.noContent().build();
  }

  /**
   * Updates the authenticated user's password.
   *
   * @param authentication the current authenticated user
   * @param passwordChangeRequest a password update request data transfer object with 
   *     credentials to update the password
   * @return a {@code 204 No Content} response if the password is updated
   */
  @PutMapping("/me/password")
  public ResponseEntity<Void> updatePassword(
      Authentication authentication,
      @RequestBody @Valid PasswordChangeRequest passwordChangeRequest
  ) {
    accountService.updatePassword(authentication.getName(), passwordChangeRequest);
    return ResponseEntity.noContent().build();
  }

  /**
   * Deletes the authenticated user's account.
   *
   * @param authentication the current authenticated user
   * @return a {@code 204 No Content} response if the account is deleted
   */
  @DeleteMapping("/me")
  public ResponseEntity<Void> deleteAccount(Authentication authentication) {
    accountService.deleteAccount(authentication.getName());
    return ResponseEntity.noContent().build();
  }
}
