package com.financedashboard.authentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data transfer object representing a request to log in to an account.
 *
 * @param username unique username given by the user
 * @param password raw, unhashed password given by the user
 */
public record LoginRequest(

    @NotBlank(message = "Username cannot be blank.") 
    @Size(max = 20, message = "Username cannot be more than 20 characters.") 
    String username,

    @NotBlank(message = "Password cannot be blank.") 
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.") 
    String password

) {}
