package com.finance_dashboard.goals;

import com.finance_dashboard.ResourceNotFoundException;
import com.finance_dashboard.accounts.Account;
import com.finance_dashboard.accounts.AccountRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;

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

    public GoalResponse getGoal(Long goalId, String username) {
        Goal goal = goalRepository.findByGoalIdAndAccountUsername(goalId, username)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find goal."));
        
        return new GoalResponse(
            goal.getGoalId(),
            goal.getName(),
            goal.getTargetAmount(),
            goal.getCurrentAmount(),
            goal.getTargetDate(),
            goal.getPriorityLevel(),
            goal.getStatus(),
            goal.getDescription());
    }

    public List<GoalResponse> getGoals(String username) {
        return goalRepository.findByAccountUsername(username)
                .stream()
                .map(goal -> new GoalResponse(
                        goal.getGoalId(),
                        goal.getName(),
                        goal.getTargetAmount(),
                        goal.getCurrentAmount(),
                        goal.getTargetDate(),
                        goal.getPriorityLevel(),
                        goal.getStatus(),
                        goal.getDescription()))
                .toList();
    }

    @Transactional
    public Goal createGoal(String username, GoalRequest request) {

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find account."));

        Goal newGoal = new Goal(
            null,
            account,
            request.name(),
            request.targetAmount(),
            request.currentAmount(),
            null,
            request.targetDate(),
            request.priorityLevel(),
            request.status(),
            request.description()
        );

        return goalRepository.save(newGoal);
    }

    @Transactional
    public void updateGoal(Long goalId, String username, GoalRequest updateRequest) {
        Goal goal = goalRepository.findByGoalIdAndAccountUsername(goalId, username)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find goal."));

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
    public void deleteGoal(Long goalId, String username) {
        Goal goal = goalRepository.findByGoalIdAndAccountUsername(goalId, username)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find goal."));

        goalRepository.delete(goal);
    }
}
