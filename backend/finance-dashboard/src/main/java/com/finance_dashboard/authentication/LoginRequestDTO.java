package com.finance_dashboard.authentication;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
    
    @NotBlank(message = "Username cannot be blank.")
    String username,

    @NotBlank(message = "Password cannot be blank.")
    String password

) {}
