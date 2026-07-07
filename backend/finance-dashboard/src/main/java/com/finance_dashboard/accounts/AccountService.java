package com.finance_dashboard.accounts;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Optional<Account> getAccountById(Long accountId) {
        return accountRepository.findById(accountId);
    }

    public Iterable<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Transactional
    public Account createAccount(AccountRegistrationRequest request) {
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("This email is already taken.");
        }

        if (accountRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (accountRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        // TODO: Do something to hash password
        String hashPassword = request.getPassword();

        Account newAccount = new Account(
                null,
                request.getUsername(),
                request.getEmail(),
                hashPassword,
                request.getPhoneNumber());

        return accountRepository.save(newAccount);
    }

    @Transactional
    public void updateAccount(Long accountId, AccountRegistrationRequest updateRequest) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Could not find account."));

        // TODO: Add more advanced logic to update the password or other information
        account.setUsername(updateRequest.getUsername());
        account.setEmail(updateRequest.getEmail());
        account.setPhoneNumber(updateRequest.getPhoneNumber());
        account.setHashPassword(updateRequest.getPassword());

        accountRepository.save(account);
    }

    @Transactional
    public void deleteAccount(Long accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new AccountNotFoundException("Could not find account.");
        }

        accountRepository.deleteById(accountId);
    }
}
