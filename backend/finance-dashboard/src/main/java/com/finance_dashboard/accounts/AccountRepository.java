package com.finance_dashboard.accounts;

import org.springframework.data.jpa.repository.JpaRepository;


public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email); 
    boolean existsByPhoneNumber(String phoneNumber);
}
