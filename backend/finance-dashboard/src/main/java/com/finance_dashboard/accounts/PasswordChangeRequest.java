package com.finance_dashboard.accounts;

import jakarta.validation.constraints.*;

public record PasswordChangeRequest(
    
    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
    String currentPassword,
    
    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
    String newPassword

) {}
