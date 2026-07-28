package com.finance_dashboard.authentication;

import com.finance_dashboard.accounts.Account;
import com.finance_dashboard.accounts.AccountRepository;
import com.finance_dashboard.accounts.AccountCreateRequest;
import com.finance_dashboard.accounts.AccountService;
import com.finance_dashboard.exceptions.ResourceNotFoundException;
import com.finance_dashboard.security.JwtService;
import com.finance_dashboard.exceptions.ValidationException;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final JwtService jwtService;

    public AuthenticationService(
            AuthenticationManager authenticationManager,
            AccountService accountService,
            AccountRepository accountRepository,
            JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.accountService = accountService;
        this.accountRepository = accountRepository;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password()));
        } catch (UsernameNotFoundException e) {
            throw new ValidationException(Map.of("username", "Username not found"));
        } catch (BadCredentialsException e) {
            throw new ValidationException(Map.of("password", "Incorrect password"));
        }

        Account account = accountRepository.findByUsername(request.username())
                .orElseThrow(() -> new ResourceNotFoundException("Could not find account."));

        account.setLastLoginTime(LocalDateTime.now());
        accountRepository.save(account);

        String token = jwtService.generateToken(account);

        return new LoginResponse(
                account.getAccountId(),
                account.getRole(),
                account.getUsername(),
                token);
    }

    public LoginResponse register(RegistrationRequest request) {

        Account account = accountService.createAccount(
                new AccountCreateRequest(
                        request.username(),
                        request.password()));

        String token = jwtService.generateToken(account);

        return new LoginResponse(
                account.getAccountId(),
                account.getRole(),
                account.getUsername(),
                token);
    }
}
