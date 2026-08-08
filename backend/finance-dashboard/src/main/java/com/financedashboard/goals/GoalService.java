package com.financedashboard.goals;

import com.financedashboard.accounts.Account;
import com.financedashboard.accounts.AccountRepository;
import com.financedashboard.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service layer for managing user goals.
 */ 
@Service
public class GoalService {

  private final GoalRepository goalRepository;
  private final AccountRepository accountRepository;

  /**
   * Creates an instance of {@code GoalService} with dependency injection.
   *
   * @param goalRepository repository for persisting and querying goals
   * @param accountRepository repository for looking up the owning account
   */
  public GoalService(GoalRepository goalRepository,
      AccountRepository accountRepository) {
    this.goalRepository = goalRepository;
    this.accountRepository = accountRepository;
  }

  /**
   * Retrieves a single goal, scoped to its owning account's username.
   *
   * @param goalId id of the goal to retrieve
   * @param username username of the account that must own the goal
   * @return a response DTO representing the goal
   * @throws ResourceNotFoundException if no goal with that id exists
   *     for the given username
   */
  public GoalResponse getGoal(Long goalId, String username) {
    Goal goal = goalRepository
        .findByGoalIdAndAccountUsername(goalId, username)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Could not find goal."));

    return new GoalResponse(goal.getGoalId(), goal.getName(), goal.getTargetAmount(),
        goal.getCurrentAmount(), goal.getTargetDate(), goal.getPriorityLevel(),
        goal.getStatus(), goal.getDescription());
  }

  /**
   * Retrieves a paginated list of goals belonging to the given username.
   *
   * @param pageable pagination and sorting parameters
   * @param username username of the account whose goals are returned
   * @return a page of response DTOs representing the account's goals
   */
  public Page<GoalResponse> getGoals(Pageable pageable, String username) {
    return goalRepository.findByAccountUsername(pageable, username)
        .map(goal -> new GoalResponse(goal.getGoalId(), goal.getName(),
            goal.getTargetAmount(), goal.getCurrentAmount(), goal.getTargetDate(),
            goal.getPriorityLevel(), goal.getStatus(), goal.getDescription()));
  }

  /**
   * Creates a new goal under the given username's account.
   *
   * @param username username of the account the goal will belong to
   * @param request validated goal fields
   * @return the newly persisted goal, including its generated id
   * @throws ResourceNotFoundException if no account exists for the given username
   */
  @Transactional
  public Goal createGoal(String username, GoalRequest request) {

    Account account = accountRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find account."));

    Goal newGoal = new Goal(null, account, request.name(), request.targetAmount(),
        request.currentAmount(), null, request.targetDate(), request.priorityLevel(),
        request.status(), request.description());

    return goalRepository.save(newGoal);
  }

  /**
   * Updates the mutable fields of a goal owned by the given username.
   *
   * @param goalId id of the goal to update
   * @param username username of the account that must own the goal
   * @param updateRequest validated fields to apply to the goal
   * @throws ResourceNotFoundException if no goal with that id exists
   *     for the given username
   */
  @Transactional
  public void updateGoal(Long goalId, String username, GoalRequest updateRequest) {
    Goal goal = goalRepository
        .findByGoalIdAndAccountUsername(goalId, username)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Could not find goal."));

    goal.setName(updateRequest.name());
    goal.setTargetAmount(updateRequest.targetAmount());
    goal.setCurrentAmount(updateRequest.currentAmount());
    goal.setTargetDate(updateRequest.targetDate());
    goal.setPriorityLevel(updateRequest.priorityLevel());
    goal.setStatus(updateRequest.status());
    goal.setDescription(updateRequest.description());

    goalRepository.save(goal);
  }

  /**
   * Deletes a goal owned by the given username.
   *
   * @param goalId id of the goal to delete
   * @param username username of the account that must own the goal
   * @throws ResourceNotFoundException if no goal with that id exists
   *     for the given username
   */
  @Transactional
  public void deleteGoal(Long goalId, String username) {
    Goal goal = goalRepository
        .findByGoalIdAndAccountUsername(goalId, username)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Could not find goal."));

    goalRepository.delete(goal);
  }
}