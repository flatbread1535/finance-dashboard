package com.finance_dashboard.goals;

import com.finance_dashboard.accounts.Account;
import com.finance_dashboard.accounts.AccountRepository;
import com.finance_dashboard.accounts.AccountNotFoundException;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class GoalService {
    
    private final GoalRepository goalRepository;
    private final AccountRepository accountRepository;

    public GoalService(
        GoalRepository goalRepository, 
        AccountRepository accountRepository
    ) {
        this.goalRepository = goalRepository;
        this.accountRepository = accountRepository;
    }

    public Optional<Goal> getGoalById(Long goalId) {
        return goalRepository.findById(goalId);
    }

    public Iterable<Goal> getAllGoals() {
        return goalRepository.findAll();
    }

    @Transactional
    public Goal createGoal(GoalRequest request) {

        Account account = accountRepository.findById(request.accountId())
                .orElseThrow(() -> new AccountNotFoundException("Could not find account."));

        Goal newGoal = new Goal(
            null,
            account,
            request.name(),
            request.targetAmount(),
            request.currentAmount(),
            LocalDateTime.now(),
            request.targetDate(),
            request.priorityLevel(),
            request.status(),
            request.description()
        );

        return goalRepository.save(newGoal);
    }

    @Transactional
    public void updateGoal(Long goalId, GoalRequest updateRequest) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new GoalNotFoundException("Could not find goal."));

        goal.setName(updateRequest.name());
        goal.setTargetAmount(updateRequest.targetAmount());
        goal.setCurrentAmount(updateRequest.currentAmount());
        goal.setTargetDate(updateRequest.targetDate());
        goal.setPriorityLevel(updateRequest.priorityLevel());
        goal.setStatus(updateRequest.status());
        goal.setDescription(updateRequest.description());
        
        goalRepository.save(goal);
    }

    @Transactional
    public void deleteGoal(Long goalId) {
        if (!goalRepository.existsById(goalId)) {
            throw new GoalNotFoundException("Could not find goal.");
        }

        goalRepository.deleteById(goalId);
    }
}
