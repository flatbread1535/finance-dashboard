package com.financedashboard.authentication;

import com.financedashboard.accounts.Account;
import com.financedashboard.accounts.AccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of the UserDetailsService for the finance dashboard.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
  private final AccountRepository accountRepository;

  /**
   * Constructs an instance of {@code CustomUserDetailsService} with dependency injection.
   *
   * @param accountRepository the data access repository handling account entities
   */
  public CustomUserDetailsService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    Account account = accountRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Could not find account."));

    return User.withUsername(account.getUsername())
        .password(account.getHashPassword())        
        .roles(account.getRole().name())
        .build();
  }
}
