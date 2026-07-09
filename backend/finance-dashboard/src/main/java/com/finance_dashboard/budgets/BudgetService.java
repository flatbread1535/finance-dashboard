package com.finance_dashboard.budgets;

import com.finance_dashboard.accounts.Account;
import com.finance_dashboard.accounts.AccountRepository;
import com.finance_dashboard.accounts.AccountNotFoundException;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BudgetService {
    
    private final BudgetRepository budgetRepository;
    private final AccountRepository accountRepository;

    public BudgetService(
        BudgetRepository budgetRepository, 
        AccountRepository accountRepository
    ) {
        this.budgetRepository = budgetRepository;
        this.accountRepository = accountRepository;
    }

    public Optional<Budget> getBudgetById(Long budgetId) {
        return budgetRepository.findById(budgetId);
    }

    public Iterable<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    @Transactional
    public Budget createBudget(BudgetRequest request) {

        Account account = accountRepository.findById(request.accountId())
                .orElseThrow(() -> new AccountNotFoundException("Could not find account."));

        Budget newBudget = new Budget(
            null,
            account,
            request.name(),
            request.category(),
            request.targetAmount(),
            request.currentSpending(),
            LocalDateTime.now(),
            request.startDate(),
            request.endDate(),
            request.isThresholdAlert(),
            request.thresholdAlertValue()
        );

        return budgetRepository.save(newBudget);
    }

    @Transactional
    public void updateBudget(Long budgetId, BudgetRequest updateRequest) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new BudgetNotFoundException("Could not find budget."));

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
    public void deleteBudget(Long budgetId) {
        if (!budgetRepository.existsById(budgetId)) {
            throw new BudgetNotFoundException("Could not find budget.");
        }

        budgetRepository.deleteById(budgetId);
    }
}
