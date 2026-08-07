package com.financedashboard.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Catches framework validation failures.
   *
   * @param e the validation exception 
   * @return a {@code 400 Bad Request} response with a bad request error map
   *     in an API error response DTO
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e
  ) {
    Map<String, String> errorMap = new HashMap<>();
    e.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String message = error.getDefaultMessage();
      errorMap.put(fieldName, message);
    });

    ApiErrorResponse error = new ApiErrorResponse(
        HttpStatus.BAD_REQUEST.value(),  
        "Validation Failed", 
        null, errorMap, 
        LocalDateTime.now()
    );

    return ResponseEntity.badRequest().body(error);
  }

  /**
   * Handles generic runtime arguments that break validation.
   *
   * @param e the runtime exception 
   * @return a {@code 400 Bad Request} response with an API error response DTO body
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(
        IllegalArgumentException e
  ) {
    ApiErrorResponse error = new ApiErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        "Illegal Argument", 
        e.getMessage(), 
        null, 
        LocalDateTime.now()
    );

    return ResponseEntity.badRequest().body(error);
  }

  /**
   * Handles when requested resources cannot be found in the database.
   *
   * @param e the runtime excetion
   * @return a {@code 404 Not Found} response with an API error response DTO
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(
        ResourceNotFoundException e) {
    ApiErrorResponse error = new ApiErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        "Resource Not Found", 
        e.getMessage(), 
        null, 
        LocalDateTime.now()
    );

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  /**
   * Handles invalid credentials.
   *
   * @param e the runtime exception
   * @return a {@code 400 Bad Request} response with a bad request error map
   *     in an API error response DTO
   */
  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ApiErrorResponse> handleValidationResponseException(
            ValidationException e
  ) {
    ApiErrorResponse error = new ApiErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        "Validation Failed", 
        null, 
        e.getErrorMap(), 
        LocalDateTime.now()
    );
    return ResponseEntity.badRequest().body(error);
  }
}
