package com.finance_dashboard.budgets;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Page<Budget> findByAccountUsername(Pageable pageable, String username);
    Optional<Budget> findByBudgetIdAndAccountUsername(Long budgetId, String username);
}
