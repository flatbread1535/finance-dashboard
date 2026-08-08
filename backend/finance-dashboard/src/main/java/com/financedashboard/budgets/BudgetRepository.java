package com.financedashboard.budgets;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Data access repository for managing Budget entities.
 */
public interface BudgetRepository extends JpaRepository<Budget, Long> {

  Page<Budget> findByAccountUsername(Pageable pageable, String username);

  Optional<Budget> findByBudgetIdAndAccountUsername(Long budgetId, String username);
}
