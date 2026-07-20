package com.finance_dashboard.budgets;

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
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import java.net.URI;

@RestController
@RequestMapping("/budgets")
public class BudgetController {
    
    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping("/{budgetId}")
    public ResponseEntity<BudgetResponse> getBudget(@PathVariable Long budgetId,  Authentication authentication) {
        return ResponseEntity.ok(budgetService.getBudget(budgetId, authentication.getName()));
    }

    @GetMapping
    public ResponseEntity<Page<BudgetResponse>> getAllBudgets(Pageable pageable, Authentication authentication) {
        return ResponseEntity.ok(budgetService.getBudgets(pageable, authentication.getName()));
    }

    @PostMapping
    public ResponseEntity<Void> createBudget(Authentication authentication, @RequestBody @Valid BudgetRequest request) {
        String username = authentication.getName();
        Budget requestedBudget = budgetService.createBudget(username, request);
        URI location = URI.create("/budgets/" + requestedBudget.getBudgetId());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{budgetId}")
    public ResponseEntity<Void> putBudget(@PathVariable Long budgetId, Authentication authentication, @RequestBody @Valid BudgetRequest updateRequest) {
        budgetService.updateBudget(budgetId, authentication.getName(), updateRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{budgetId}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long budgetId, Authentication authentication) {
        budgetService.deleteBudget(budgetId, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
