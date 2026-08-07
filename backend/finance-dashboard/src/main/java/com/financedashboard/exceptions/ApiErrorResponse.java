package com.financedashboard.exceptions;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Data transfer object representing an error response 
 * returned by REST endpoints.
 *
 * @param status the HTTP status code
 * @param error the phrase matching the HTTP status
 * @param message summary text explaining the cause of the error
 * @param errorMap a collection of targeted field-level validation errors;
 *     keys represent invalid request fields and values provide validation 
 *     messages
 * @param timestamp the exact date and time the error occurred
 */
public record ApiErrorResponse(
        int status, 
        String error, 
        String message,
        Map<String, String> errorMap, 
        LocalDateTime timestamp
) {}
