package com.finance_dashboard.accounts;

import jakarta.validation.constraints.*;

public record AccountRequestDTO(
    
    @NotBlank(message = "Username cannot be blank.")
    @Size(max = 20, message = "Username cannot be more than 20 characters.")
    String username,

    @NotBlank(message = "Email cannot be blank.")
    @Email(message = "Invalid email format.")
    String email,

    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
    String password,
    
    // TODO: Phone number custom validation
    @NotBlank(message = "Phone number cannot be blank.")
    String phoneNumber

) {}
