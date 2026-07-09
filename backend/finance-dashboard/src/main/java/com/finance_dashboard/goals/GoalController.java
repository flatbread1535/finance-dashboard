package com.finance_dashboard.goals;

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
@RequestMapping("/goals")
public class GoalController {
    
    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @GetMapping("/{goalId}")
    public ResponseEntity<Goal> findById(@PathVariable Long goalId) {
        Optional<Goal> goalOptional = goalService.getGoalById(goalId);
        if (goalOptional.isPresent()) {
            return ResponseEntity.ok(goalOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Iterable<Goal>> findAll() {
        return ResponseEntity.ok(goalService.getAllGoals());
    }

    @PostMapping
    public ResponseEntity<Void> createGoal(@RequestBody GoalRequest request) {
        Goal requestedGoal = goalService.createGoal(request);
        URI location = URI.create("/goals/" + requestedGoal.getGoalId());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{goalId}")
    public ResponseEntity<Void> putGoal(@PathVariable Long goalId, @RequestBody GoalRequest updateRequest) {
        try {
            goalService.updateGoal(goalId, updateRequest);
            return ResponseEntity.noContent().build();
        } catch (GoalNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{goalId}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long goalId) {
        try {
            goalService.deleteGoal(goalId);
            return ResponseEntity.noContent().build();
        } catch (GoalNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
