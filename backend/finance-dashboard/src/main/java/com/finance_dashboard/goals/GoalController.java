package com.finance_dashboard.goals;

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
import java.util.List;

@RestController
@RequestMapping("/goals")
public class GoalController {
    
    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @GetMapping("/{goalId}")
    public ResponseEntity<GoalResponse> findById(@PathVariable Long goalId, Authentication authentication) {
        return ResponseEntity.ok(goalService.getGoal(goalId, authentication.getName()));
    }

    @GetMapping
    public ResponseEntity<List<GoalResponse>> getAllGoals(Authentication authentication) {
        return ResponseEntity.ok(goalService.getGoals(authentication.getName()));
    }

    @PostMapping
    public ResponseEntity<Void> createGoal(Authentication authentication, @RequestBody @Valid GoalRequest request) {
        String username = authentication.getName();
        Goal requestedGoal = goalService.createGoal(username, request);
        URI location = URI.create("/goals/" + requestedGoal.getGoalId());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{goalId}")
    public ResponseEntity<Void> updateGoal(@PathVariable Long goalId, Authentication authentication, @RequestBody @Valid GoalRequest updateRequest) {
            goalService.updateGoal(goalId, authentication.getName(), updateRequest);
            return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{goalId}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long goalId, Authentication authentication) {
            goalService.deleteGoal(goalId, authentication.getName());
            return ResponseEntity.noContent().build();
    }
}
