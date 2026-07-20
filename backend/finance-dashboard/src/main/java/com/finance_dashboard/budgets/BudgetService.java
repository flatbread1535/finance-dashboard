package com.finance_dashboard.budgets;

import com.finance_dashboard.ResourceNotFoundException;
import com.finance_dashboard.accounts.Account;
import com.finance_dashboard.accounts.AccountRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final AccountRepository accountRepository;

    public BudgetService(
            BudgetRepository budgetRepository,
            AccountRepository accountRepository) {
        this.budgetRepository = budgetRepository;
        this.accountRepository = accountRepository;
    }

    public BudgetResponse getBudget(Long budgetId, String username) {
        Budget budget = budgetRepository.findByBudgetIdAndAccountUsername(budgetId, username)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find budget."));

        return new BudgetResponse(
                budget.getBudgetId(),
                budget.getName(),
                budget.getCategory(),
                budget.getTargetAmount(),
                budget.getCurrentSpending(),
                budget.getTimeCreated(),
                budget.getStartDate(),
                budget.getEndDate(),
                budget.getIsThresholdAlert(),
                budget.getThresholdAlertValue());
    }

    public Page<BudgetResponse> getBudgets(Pageable pageable, String username) {
        return budgetRepository.findByAccountUsername(pageable, username)
                .map(budget -> new BudgetResponse(
                        budget.getBudgetId(),
                        budget.getName(),
                        budget.getCategory(),
                        budget.getTargetAmount(),
                        budget.getCurrentSpending(),
                        budget.getTimeCreated(),
                        budget.getStartDate(),
                        budget.getEndDate(),
                        budget.getIsThresholdAlert(),
                        budget.getThresholdAlertValue()));
    }

    @Transactional
    public Budget createBudget(String username, BudgetRequest request) {
        if (request.startDate().isAfter(request.endDate())) {
                throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find account."));

        Budget budget = new Budget(
                null,
                account,
                request.name(),
                request.category(),
                request.targetAmount(),
                request.currentSpending(),
                null,
                request.startDate(),
                request.endDate(),
                request.isThresholdAlert(),
                request.thresholdAlertValue());

        return budgetRepository.save(budget);
    }

    @Transactional
    public void updateBudget(Long budgetId, String username, BudgetRequest updateRequest) {
        Budget budget = budgetRepository.findByBudgetIdAndAccountUsername(budgetId, username)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find budget."));
                
        budget.setName(updateRequest.name());
        budget.setCategory(updateRequest.category());
        budget.setTargetAmount(updateRequest.targetAmount());
        budget.setCurrentSpending(updateRequest.currentSpending());
        budget.setStartDate(updateRequest.startDate());
        budget.setEndDate(updateRequest.endDate());
        budget.setIsThresholdAlert(updateRequest.isThresholdAlert());
        budget.setThresholdAlertValue(updateRequest.thresholdAlertValue());

        budgetRepository.save(budget);
    }
 
    @Transactional
    public void deleteBudget(Long budgetId, String username) {
        Budget budget = budgetRepository.findByBudgetIdAndAccountUsername(budgetId, username)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find budget."));

        budgetRepository.delete(budget);
    }
}
