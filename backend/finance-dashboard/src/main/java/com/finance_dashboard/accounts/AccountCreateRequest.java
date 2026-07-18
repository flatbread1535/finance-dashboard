package com.finance_dashboard.accounts;

import jakarta.validation.constraints.*;

public record AccountCreateRequest(
    
    @NotBlank(message = "Username cannot be blank.")
    @Size(max = 20, message = "Username cannot be more than 20 characters.")
    String username,

    @NotBlank(message = "Email cannot be blank.")
    @Size(max = 100, message = "Email cannot be more than 100 characters.")
    @Email(message = "Invalid email format.")
    String email,
    
    // TODO: Phone number custom validation
    @NotBlank(message = "Phone number cannot be blank.")
    @Size(max = 20, message = "Phone number cannot be more than 20 characters.")
    String phoneNumber,

    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
    String password

) {}
