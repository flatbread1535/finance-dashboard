package com.finance_dashboard.goals;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalRepository extends JpaRepository<Goal, Long> {

    List<Goal> findByAccountUsername(String username);
    Optional<Goal> findByGoalIdAndAccountUsername(Long goalId, String username);
}
