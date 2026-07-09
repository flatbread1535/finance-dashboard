package com.finance_dashboard.budgets;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BudgetNotFoundException extends RuntimeException {
    
    public BudgetNotFoundException(String message) {
        super(message);
    }
}

