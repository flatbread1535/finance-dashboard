package com.finance_dashboard.accounts;

import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.finance_dashboard.ResourceNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

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
                account.getLastLoginTime());
    }

    public AccountResponse getAccountByUsername(String username) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find account."));

        return newAccountResponse(account);
    }

    public AccountResponse getAccountById(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find account."));

        return newAccountResponse(account);
    }

    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAll().stream().map(this::newAccountResponse).toList();
    }

    private void validateUniqueFieldsForCreation(String username, String email, String phoneNumber) {
        if (accountRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("This email is already taken.");
        }

        if (accountRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (accountRepository.existsByPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Phone number already exists");
        }
    }

    @Transactional
    public Account createAccount(AccountCreateRequest request) {
        validateUniqueFieldsForCreation(request.username(), request.email(), request.phoneNumber());

        String hashPassword = passwordEncoder.encode(request.password());

        Account newAccount = new Account(
                null,
                Role.USER,
                request.username(),
                request.email(),
                request.phoneNumber(),
                null,
                false,
                null,
                null,
                hashPassword);

        return accountRepository.save(newAccount);
    }

    private void validateUniqueFieldsForUpdate(Account account, AccountUpdateRequest request) {
        if (accountRepository.existsByUsernameAndAccountIdNot(request.username(), account.getAccountId())) {
            throw new IllegalArgumentException("Username already exists.");
        }

        if (accountRepository.existsByEmailAndAccountIdNot(request.email(), account.getAccountId())) {
            throw new IllegalArgumentException("Email already exists.");
        }

        if (accountRepository.existsByPhoneNumberAndAccountIdNot(request.phoneNumber(), account.getAccountId())) {
            throw new IllegalArgumentException("Phone number already exists.");
        }
    }

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

    @Transactional
    public void updatePassword(String username, PasswordChangeRequest passwordChangeRequest) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find account."));

        if (!passwordEncoder.matches(passwordChangeRequest.currentPassword(), account.getHashPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }

        if (passwordEncoder.matches(passwordChangeRequest.newPassword(), account.getHashPassword())) {
            throw new IllegalArgumentException("New password matches current password.");
        }

        String newHashPassword = passwordEncoder.encode(passwordChangeRequest.newPassword());
        account.setHashPassword(newHashPassword);
    }

    @Transactional
    public void deleteAccount(String username) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find account."));

        accountRepository.delete(account);
    }

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
