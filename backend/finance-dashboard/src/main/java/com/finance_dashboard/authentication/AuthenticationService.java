package com.finance_dashboard.authentication;

import com.finance_dashboard.ResourceNotFoundException;
import com.finance_dashboard.accounts.Account;
import com.finance_dashboard.accounts.AccountRepository;
import com.finance_dashboard.accounts.AccountRequestDTO;
import com.finance_dashboard.accounts.AccountService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    public AuthenticationService(
            AuthenticationManager authenticationManager,
            AccountService accountService,
            AccountRepository accountRepository) {
        this.authenticationManager = authenticationManager;
        this.accountService = accountService;
        this.accountRepository = accountRepository;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        if (!authentication.isAuthenticated()) {
            throw new IllegalArgumentException("Invalid username or password.");
        }

        Account account = accountRepository.findByUsername(request.username())
                .orElseThrow(() -> new ResourceNotFoundException("Could not find account."));

        return new LoginResponseDTO(account.getAccountId(), account.getUsername());
    }

    public LoginResponseDTO register(RegistrationRequestDTO request) {

        Account account = accountService.createAccount(
                new AccountRequestDTO(
                        request.username(),
                        request.email(),
                        request.password(),
                        request.phoneNumber()));

        return new LoginResponseDTO(
                account.getAccountId(),
                account.getUsername());
    }
}
