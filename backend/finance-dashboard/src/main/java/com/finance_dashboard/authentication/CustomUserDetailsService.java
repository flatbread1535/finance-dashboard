package com.finance_dashboard.authentication;

import com.finance_dashboard.ResourceNotFoundException;
import com.finance_dashboard.accounts.Account;
import com.finance_dashboard.accounts.AccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public CustomUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find account."));

        return User.withUsername(account.getUsername())
                .password(account.getHashPassword())
                .roles("USER")
                .build();
    }
}
