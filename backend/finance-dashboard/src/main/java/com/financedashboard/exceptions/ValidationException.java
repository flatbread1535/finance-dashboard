package com.financedashboard.exceptions;

import java.util.Map;

/**
 * Exception thrown when validation of credentials fails.
 */
public class ValidationException extends RuntimeException {
  private final Map<String, String> errorMap;

  /**
   * Constructs a new {@code ValidationException}.
   *
   * @param errorMap a collection here keys represent invalid form field names
   *     and values represent descriptions of the errors
   */
  public ValidationException(Map<String, String> errorMap) {
    super("Validation failed.");
    this.errorMap = errorMap;
  }

  /**
   * Retrieves a map of the invalid form field names and error descriptions.
   *
   * @return a map pairing field names and their corresponding messages
   */
  public Map<String, String> getErrorMap() {
    return errorMap;
  }
}
