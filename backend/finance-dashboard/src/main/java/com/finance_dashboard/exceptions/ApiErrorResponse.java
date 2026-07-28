package com.finance_dashboard.exceptions;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiErrorResponse(
    int status,
    String error,
    String message,
    Map<String, String> errorMap,
    LocalDateTime timestamp
) {}
