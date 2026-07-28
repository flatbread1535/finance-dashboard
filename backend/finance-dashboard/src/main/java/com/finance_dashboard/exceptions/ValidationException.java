package com.finance_dashboard.exceptions;

import java.util.Map;

public class ValidationException extends RuntimeException {
    private final Map<String, String> errorMap;

    public ValidationException(Map<String, String> errorMap) {
        super("Validation failed.");
        this.errorMap = errorMap;
    }

    public Map<String, String> getErrorMap() {
        return errorMap;
    }
}
