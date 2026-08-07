package com.financedashboard.accounts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data transfer object representing a request to update password.
 *
 * @param currentPassword the current account's password used as credentials
 * @param newPassword the new password to be used
 */
public record PasswordChangeRequest(

    @NotBlank(message = "Password cannot be blank.") 
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.") 
    String currentPassword,

    @NotBlank(message = "Password cannot be blank.") 
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.") 
    String newPassword

) {}
