package com.financedashboard.goals;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Data access repository for managing Budget entities.
 */
public interface GoalRepository extends JpaRepository<Goal, Long> {

  Page<Goal> findByAccountUsername(Pageable pageable, String username);

  Optional<Goal> findByGoalIdAndAccountUsername(Long goalId, String username);
}
