package com.finance_dashboard.budgets;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/budgets")
public class BudgetController {
    
    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping("/{budgetId}")
    public ResponseEntity<Budget> findById(@PathVariable Long budgetId) {
        Optional<Budget> budgetOptional = budgetService.getBudgetById(budgetId);
        if (budgetOptional.isPresent()) {
            return ResponseEntity.ok(budgetOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Iterable<Budget>> findAll() {
        return ResponseEntity.ok(budgetService.getAllBudgets());
    }

    @PostMapping
    public ResponseEntity<Void> createBudget(@RequestBody BudgetRequest request) {
        Budget requestedBudget = budgetService.createBudget(request);
        URI location = URI.create("/budgets/" + requestedBudget.getBudgetId());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{budgetId}")
    public ResponseEntity<Void> putBudget(@PathVariable Long budgetId, @RequestBody BudgetRequest updateRequest) {
        try {
            budgetService.updateBudget(budgetId, updateRequest);
            return ResponseEntity.noContent().build();
        } catch (BudgetNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{budgetId}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long budgetId) {
        try {
            budgetService.deleteBudget(budgetId);
            return ResponseEntity.noContent().build();
        } catch (BudgetNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
