package com.financedashboard.exceptions;

/**
 * Exception thrown when a requested database entity cannot be found.
 */
public class ResourceNotFoundException extends RuntimeException {

  /**
   * Constructs a new {@code ResourceNotFoundException} with a given 
   * error message.
   *
   * @param message a descriptive message describing that the resource
   *     cannot be found
   */
  public ResourceNotFoundException(String message) {
    super(message);
  }
}
