package com.financedashboard.budgets;

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
 * A REST controller exposing CRUD endpoints for budgets.
 */
@RestController
@RequestMapping("/budgets")
public class BudgetController {

  private final BudgetService budgetService;

  /**
   * Constructs an instance of {@code BudgetController} 
   * with dependency injection.
   *
   * @param budgetService service layer component that handles 
   *     core business logic for budgets
   */
  public BudgetController(BudgetService budgetService) {
    this.budgetService = budgetService;
  }

  /**
   * Retrieves a single budget belonging to an authenticated account.
   *
   * @param budgetId the primary key identifier of the budget to be retrieved
   * @param authentication the currently authenticated account
   * @return a {@code 200 OK} response with a budget response DTO body
   */
  @GetMapping("/{budgetId}")
  public ResponseEntity<BudgetResponse> getBudget(
      @PathVariable Long budgetId,
      Authentication authentication) {
    return ResponseEntity.ok(budgetService.getBudget(budgetId, authentication.getName()));
  }

  /**
   * Retrieves all budgets' information with pagination support.
   *
   * @param pageable an object containing page number, page size, and sorting information
   * @param authentication the currently authenticated user
   * @return a {@code 200 OK} response with containing a page of budget response 
   *     data transfer objects as the body
   */
  @GetMapping
  public ResponseEntity<Page<BudgetResponse>> getAllBudgets(
      Pageable pageable,
      Authentication authentication) {
    return ResponseEntity.ok(budgetService.getBudgets(pageable, authentication.getName()));
  }

  /**
   * Creates a new budget for the authenticated account.
   *
   * @param authentication the currently authenticated account
   * @param request a request DTO with fields of data used to construct a new 
   *     budget entity
   * @return a {@code 201 Created} response containing the location header of the 
   *     new budget
   */
  @PostMapping
  public ResponseEntity<Void> createBudget(
      Authentication authentication,
      @RequestBody @Valid BudgetRequest request
  ) {
    String username = authentication.getName();
    Budget requestedBudget = budgetService.createBudget(username, request);
    URI location = URI.create("/budgets/" + requestedBudget.getBudgetId());
    return ResponseEntity.created(location).build();
  }

  /**
   * Updates an existing budget owned by the authenticated account.
   *
   * @param budgetId primary key identifier of the budget to be updated
   * @param authentication the currently authenticated account
   * @param updateRequest a request DTO with fields of data used to update the budget
   * @return a {@code 204 No Content} response once the update succeeds
   */
  @PutMapping("/{budgetId}")
  public ResponseEntity<Void> putBudget(
      @PathVariable Long budgetId,
      Authentication authentication, 
      @RequestBody @Valid BudgetRequest updateRequest
  ) {
    budgetService.updateBudget(budgetId, authentication.getName(), updateRequest);
    return ResponseEntity.noContent().build();
  }

  /**
   * Deletes a budget owned by an authenticated account.
   *
   * @param budgetId primary key identifier of the budget to be deleted
   * @param authentication the currently authenticated account
   * @return a {@code 204 No Content} response once the deletion succeeds
   */
  @DeleteMapping("/{budgetId}")
  public ResponseEntity<Void> deleteBudget(
      @PathVariable Long budgetId,
      Authentication authentication) {
    budgetService.deleteBudget(budgetId, authentication.getName());
    return ResponseEntity.noContent().build();
  }
}
