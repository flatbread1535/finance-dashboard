package com.finance_dashboard.authentication;

import jakarta.validation.constraints.*;

public record LoginRequest(
    
    @NotBlank(message = "Username cannot be blank.")   
    @Size(max = 20, message = "Username cannot be more than 20 characters.")
    String username,

    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
    String password

) {}
