package com.financedashboard.budgets;

import com.financedashboard.accounts.Account;
import com.financedashboard.accounts.AccountRepository;
import com.financedashboard.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service layer for managing user budgets.
 */ 
@Service
public class BudgetService {

  private final BudgetRepository budgetRepository;
  private final AccountRepository accountRepository;

  /**
   * Creates an instance of {@code BudgetService} with dependency injection.
   *
   * @param budgetRepository repository for persisting and querying budgets
   * @param accountRepository repository for looking up the owning account
   */
  public BudgetService(BudgetRepository budgetRepository,
      AccountRepository accountRepository) {
    this.budgetRepository = budgetRepository;
    this.accountRepository = accountRepository;
  }

  /**
   * Retrieves a single budget, scoped to its owning account's username.
   *
   * @param budgetId id of the budget to retrieve
   * @param username username of the account that must own the budget
   * @return a response DTO representing the budget
   * @throws ResourceNotFoundException if no budget with that id exists
   *     for the given username
   */
  public BudgetResponse getBudget(Long budgetId, String username) {
    Budget budget = budgetRepository
        .findByBudgetIdAndAccountUsername(budgetId, username)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Could not find budget."));

    return new BudgetResponse(budget.getBudgetId(), budget.getName(),
        budget.getCategory(), budget.getTargetAmount(),
        budget.getCurrentSpending(), budget.getTimeCreated(),
        budget.getStartDate(), budget.getEndDate(),
        budget.getIsThresholdAlert(), budget.getThresholdAlertValue());
  }

  /**
   * Retrieves a paginated list of budgets belonging to the given username.
   *
   * @param pageable pagination and sorting parameters
   * @param username username of the account whose budgets are returned
   * @return a page of response DTOs representing the account's budgets
   */
  public Page<BudgetResponse> getBudgets(Pageable pageable, String username) {
    return budgetRepository.findByAccountUsername(pageable, username)
        .map(budget -> new BudgetResponse(budget.getBudgetId(),
            budget.getName(), budget.getCategory(),
            budget.getTargetAmount(),
            budget.getCurrentSpending(),
            budget.getTimeCreated(), budget.getStartDate(),
            budget.getEndDate(), budget.getIsThresholdAlert(),
            budget.getThresholdAlertValue()));
  }

  /**
   * Creates a new budget under the given username's account.
   *
   * @param username username of the account the budget will belong to
   * @param request validated budget fields
   * @return the newly persisted budget, including its generated id
   * @throws IllegalArgumentException if the start date is after the end date
   * @throws ResourceNotFoundException if no account exists for the given username
   */
  @Transactional
  public Budget createBudget(String username, BudgetRequest request) {
    if (request.startDate().isAfter(request.endDate())) {
      throw new IllegalArgumentException("Start date cannot be after end date.");
    }

    Account account = accountRepository.findByUsername(username).orElseThrow(
        () -> new ResourceNotFoundException("Could not find account."));

    Budget budget = new Budget(null, account, request.name(), request.category(),
        request.targetAmount(), request.currentSpending(), null,
        request.startDate(), request.endDate(), request.isThresholdAlert(),
        request.thresholdAlertValue());

    return budgetRepository.save(budget);
  }

  /**
   * Updates the mutable fields of a budget owned by the given username.
   *
   * @param budgetId id of the budget to update
   * @param username username of the account that must own the budget
   * @param updateRequest validated fields to apply to the budget
   * @throws ResourceNotFoundException if no budget with that id exists
   *     for the given username
   */
  @Transactional
  public void updateBudget(Long budgetId, String username, BudgetRequest updateRequest) {
    Budget budget = budgetRepository
        .findByBudgetIdAndAccountUsername(budgetId, username)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Could not find budget."));

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

  /**
   * Deletes a budget owned by the given username.
   *
   * @param budgetId id of the budget to delete
   * @param username username of the account that must own the budget
   * @throws ResourceNotFoundException if no budget with that id exists
   *     for the given username
   */
  @Transactional
  public void deleteBudget(Long budgetId, String username) {
    Budget budget = budgetRepository
        .findByBudgetIdAndAccountUsername(budgetId, username)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Could not find budget."));

    budgetRepository.delete(budget);
  }
}
