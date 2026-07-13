package com.finance_dashboard.accounts;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.finance_dashboard.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<AccountResponseDTO> getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .map(account -> new AccountResponseDTO(
                    account.getAccountId(),
                    account.getUsername(),
                    account.getEmail(),
                    account.getPhoneNumber()
                ));
    }

    @Transactional
    public Account createAccount(AccountRequestDTO request) {
        if (accountRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("This email is already taken.");
        }

        if (accountRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (accountRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        String hashPassword = passwordEncoder.encode(request.password());

        Account newAccount = new Account(
                null,
                request.username(),
                request.email(),
                hashPassword,
                request.phoneNumber());

        return accountRepository.save(newAccount);
    }

    @Transactional
    public void updateAccount(Long accountId, AccountRequestDTO updateRequest) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find account."));

        // TODO: Add more advanced logic to update information, particularly prevent duplicates
        account.setUsername(updateRequest.username());
        account.setEmail(updateRequest.email());
        account.setPhoneNumber(updateRequest.phoneNumber());
        account.setHashPassword(passwordEncoder.encode(updateRequest.password()));

        accountRepository.save(account);
    }

    @Transactional
    public void deleteAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find account."));

        accountRepository.delete(account);
    }
}
