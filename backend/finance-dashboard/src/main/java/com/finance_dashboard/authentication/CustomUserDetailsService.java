package com.finance_dashboard.authentication;

import com.finance_dashboard.accounts.Account;
import com.finance_dashboard.accounts.AccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public CustomUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    // Returns a UserDetails object Spring understands, fetches Account security data from database
    public UserDetails loadUserByUsername(String username) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Could not find account."));

        // User class translates database fields into UserDetails object
        // Maps username to security context
        return User.withUsername(account.getUsername())
                .password(account.getHashPassword())
                .roles(account.getRole().name())
                .build();
    } 
}
