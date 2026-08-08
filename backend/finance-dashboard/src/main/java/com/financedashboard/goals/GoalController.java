package com.financedashboard.goals;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A REST controller exposing CRUD endpoints for goals.
 */
@RestController
@RequestMapping("/goals")
public class GoalController {

  private final GoalService goalService;

  /**
   * Constructs an instance of {@code GoalController} 
   * with dependency injection.
   *
   * @param goalService service layer component that handles 
   *     core business logic for goals
   */
  public GoalController(GoalService goalService) {
    this.goalService = goalService;
  }

  /**
   * Retrieves a single goal belonging to an authenticated account.
   *
   * @param goalId the primary key identifier of the goal to be retrieved
   * @param authentication the currently authenticated account
   * @return a {@code 200 OK} response with a goal response DTO body
   */
  @GetMapping("/{goalId}")
  public ResponseEntity<GoalResponse> getGoal(
      @PathVariable Long goalId,
      Authentication authentication) {
    return ResponseEntity.ok(goalService.getGoal(goalId, authentication.getName()));
  }

  /**
   * Retrieves all goals' information with pagination support.
   *
   * @param pageable an object containing page number, page size, and sorting information
   * @param authentication the currently authenticated user
   * @return a {@code 200 OK} response with containing a page of goal response 
   *     data transfer objects as the body
   */
  @GetMapping
  public ResponseEntity<Page<GoalResponse>> getAllGoals(
      Pageable pageable,
      Authentication authentication) {
    return ResponseEntity.ok(goalService.getGoals(pageable, authentication.getName()));
  }

  /**
   * Creates a new goal for the authenticated account.
   *
   * @param authentication the currently authenticated account
   * @param request a request DTO with fields of data used to construct a new 
   *     goal entity
   * @return a {@code 201 Created} response containing the location header of the 
   *     new goal
   */
  @PostMapping
  public ResponseEntity<Void> createGoal(
      Authentication authentication,
      @RequestBody @Valid GoalRequest request
  ) {
    String username = authentication.getName();
    Goal requestedGoal = goalService.createGoal(username, request);
    URI location = URI.create("/goals/" + requestedGoal.getGoalId());
    return ResponseEntity.created(location).build();
  }

  /**
   * Updates an existing goal owned by the authenticated account.
   *
   * @param goalId primary key identifier of the goal to be updated
   * @param authentication the currently authenticated account
   * @param updateRequest a request DTO with fields of data used to update the goal
   * @return a {@code 204 No Content} response once the update succeeds
   */
  @PutMapping("/{goalId}")
  public ResponseEntity<Void> putGoal(
      @PathVariable Long goalId,
      Authentication authentication, 
      @RequestBody @Valid GoalRequest updateRequest
  ) {
    goalService.updateGoal(goalId, authentication.getName(), updateRequest);
    return ResponseEntity.noContent().build();
  }

  /**
   * Deletes a goal owned by an authenticated account.
   *
   * @param goalId primary key identifier of the goal to be deleted
   * @param authentication the currently authenticated account
   * @return a {@code 204 No Content} response once the deletion succeeds
   */
  @DeleteMapping("/{goalId}")
  public ResponseEntity<Void> deleteGoal(
      @PathVariable Long goalId,
      Authentication authentication) {
    goalService.deleteGoal(goalId, authentication.getName());
    return ResponseEntity.noContent().build();
  }
}
