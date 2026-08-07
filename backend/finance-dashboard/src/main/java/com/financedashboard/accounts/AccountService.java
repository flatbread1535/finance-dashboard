package com.financedashboard.accounts;

import com.financedashboard.exceptions.ResourceNotFoundException;
import com.financedashboard.exceptions.ValidationException;
import jakarta.transaction.Transactional;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service layer component that handles core business logic for accounts.
 */
@Service
public class AccountService {
  private final AccountRepository accountRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * Constructs an instance of {@code AccountService} with dependency injection.
   *
   * @param accountRepository the data access repository handling account entities
   * @param passwordEncoder the encoder engine used to hash account passwords
   */
  public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
    this.accountRepository = accountRepository;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Maps an Account entity into an AccountResponse record.
   *
   * @param account the Account entity containing raw account data
   * @return the account response data transfer object
   */
  private AccountResponse newAccountResponse(Account account) {
    return new AccountResponse(
        account.getAccountId(), 
        account.getRole(), 
        account.getUsername(),
        account.getEmail(), 
        account.getPhoneNumber(), 
        account.getProfilePictureUrl(),
        account.getDarkModeEnabled(), 
        account.getTimeCreated(), 
        account.getLastLoginTime()
    );
  }

  /**
   * Validates requested account updates to make sure they do not conflict with 
   * other accounts' information.
   *
   * @param account the account undergoing updates
   * @param request the update request data transfer object with new credentials
   * @throws IllegalArgumentException if the username, email, or phone number is claimed by 
   *     another account
   */
  private void validateUniqueFieldsForUpdate(Account account, AccountUpdateRequest request) {
    if (accountRepository.existsByUsernameAndAccountIdNot(request.username(),
        account.getAccountId())) {
      throw new IllegalArgumentException("Username already exists.");
    }

    if (accountRepository.existsByEmailAndAccountIdNot(request.email(),
        account.getAccountId())) {
      throw new IllegalArgumentException("Email already exists.");
    }

    if (accountRepository.existsByPhoneNumberAndAccountIdNot(request.phoneNumber(),
        account.getAccountId())) {
      throw new IllegalArgumentException("Phone number already exists.");
    }
  }

  /**
   * Retrieves information of a specific account with username identifier.
   *
   * @param username the identifier to retrieve the account
   * @return the account response data transfer object
   * @throws ResourceNotFoundException if an account cannot be found with the provided username 
   */
  public AccountResponse getAccountByUsername(String username) {
    Account account = accountRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find account."));

    return newAccountResponse(account);
  }

  /**
   * Retrieves information of a specific account with primary key identifier.
   *
   * @param accountId the primary key of the account to be retrieved
   * @return the account response data transfer object
   * @throws ResourceNotFoundException if an account cannot be found with the provided id
   */
  public AccountResponse getAccountById(Long accountId) {
    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find account."));

    return newAccountResponse(account);
  }

  /**
   * Retrieves all accounts' information with pagination support.
   *
   * @param pageable an object containing page number, page size, and sorting information
   * @return a page of account response data transfer objects
   */
  public Page<AccountResponse> getAllAccounts(Pageable pageable) {
    return accountRepository.findAll(pageable).map(this::newAccountResponse);
  }

  /**
   * Creates a brand new account.
   *
   * @param request the account request data transfer object containing registration fields
   *     and raw password data
   * @return the newly saved database Account entity 
   * @throws ValidationException if the provided username is already taken by another account
   */
  @Transactional
  public Account createAccount(AccountCreateRequest request) {
    if (accountRepository.existsByUsername(request.username())) {
      throw new ValidationException(Map.of("username", "Username already taken"));
    }

    String hashPassword = passwordEncoder.encode(request.password());

    Account newAccount = new Account(
        null, 
        Role.USER, 
        request.username(), 
        null, 
        null, 
        null,
        false, 
        null, 
        null, 
        hashPassword
    );

    return accountRepository.save(newAccount);
  }

  /**
   * Updates account information.
   *
   * @param username the identifier to retrieve the account
   * @param updateRequest data transfer object containing the fields to be updated
   * @throws ResourceNotFoundException if an account cannot be found with the provided username
   * @throws IllegalArgumentException if any credentials in the request conflict with other accounts
   */
  @Transactional
  public void updateAccount(String username, AccountUpdateRequest updateRequest) {
    Account account = accountRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find account."));

    validateUniqueFieldsForUpdate(account, updateRequest);

    account.setUsername(updateRequest.username());
    account.setEmail(updateRequest.email());
    account.setPhoneNumber(updateRequest.phoneNumber());
    account.setProfilePictureUrl(updateRequest.profilePictureUrl());
    account.setDarkModeEnabled(updateRequest.darkModeEnabled());

    accountRepository.save(account);
  }

  /**
   * Replaces an account's password after validating password credentials.
   *
   * @param username the identifier to retrieve the account
   * @param passwordChangeRequest data transfer object containing the old and new password
   * @throws ResourceNotFoundException if an account cannot be found with the provided username
   * @throws IllegalArgumentException if the current password validation check fails, or the new
   *     password matches the current password
   */
  @Transactional
  public void updatePassword(String username, PasswordChangeRequest passwordChangeRequest) {
    Account account = accountRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find account."));

    if (!passwordEncoder.matches(passwordChangeRequest.currentPassword(),
        account.getHashPassword())) {
      throw new IllegalArgumentException("Current password is incorrect.");
    }

    if (passwordEncoder.matches(passwordChangeRequest.newPassword(),
        account.getHashPassword())) {
      throw new IllegalArgumentException("New password matches current password.");
    }

    String newHashPassword = passwordEncoder.encode(passwordChangeRequest.newPassword());
    account.setHashPassword(newHashPassword);
  }

  /**
   * Permanently deletes an account from the database with username identifier.
   *
   * @param username the identifier to retrieve the account to be deleted
   * @throws ResourceNotFoundException if the account cannot be found with the provided username
   */
  @Transactional
  public void deleteAccount(String username) {
    Account account = accountRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find account."));

    accountRepository.delete(account);
  }

  /**
   * Permanently deletes an account from the database with primary key identifier.
   *
   * @param accountId the primary key of the account to be deleted
   * @throws ResourceNotFoundException if the account cannot be found with the provided id
   * @throws IllegalArgumentException if the account to be deleted holds an ADMIN role
   */
  @Transactional
  public void deleteAccountById(Long accountId) {
    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find account."));

    if (account.getRole() == Role.ADMIN) {
      throw new IllegalArgumentException("Cannot delete an ADMIN account.");
    }

    accountRepository.deleteById(accountId);
  }
}
