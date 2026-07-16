package com.finance_dashboard.accounts;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.finance_dashboard.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AccountResponseDTO getAccountByUsername(String username) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find account."));

        return new AccountResponseDTO(
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

    @Transactional
    public Account createAccount(AccountRequestDTO request) {
        validateUniqueFields(request.username(), request.email(), request.phoneNumber());

        String hashPassword = passwordEncoder.encode(request.password());

        Account newAccount = new Account(
                null,
                Role.USER,
                request.username(),
                request.email(),
                request.phoneNumber(),
                null,
                false,
                LocalDateTime.now(),
                LocalDateTime.now(),
                hashPassword);

        return accountRepository.save(newAccount);
    }

    private void validateUniqueFields(String username, String email, String phoneNumber) {
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
    public void updateAccount(String username, AccountRequestDTO updateRequest) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find account."));

        // TODO: Add more advanced logic to update information, particularly prevent
        // duplicates
        account.setUsername(updateRequest.username());
        account.setEmail(updateRequest.email());
        account.setPhoneNumber(updateRequest.phoneNumber());
        account.setHashPassword(passwordEncoder.encode(updateRequest.password()));

        accountRepository.save(account);
    }

    @Transactional
    public void deleteAccount(String username) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find account."));

        accountRepository.delete(account);
    }
}
