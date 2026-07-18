package com.finance_dashboard.budgets;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByAccountUsername(String username);
    Optional<Budget> findByBudgetIdAndAccountUsername(Long budgetId, String username);
}
