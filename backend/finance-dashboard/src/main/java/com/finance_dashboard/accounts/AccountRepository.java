package com.finance_dashboard.accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<Account> findByUsername(String username);
    boolean existsByUsernameAndAccountIdNot(String username, Long accountId);
    boolean existsByEmailAndAccountIdNot(String email, Long accountId);
    boolean existsByPhoneNumberAndAccountIdNot(String phoneNumber, Long accountId);
}
