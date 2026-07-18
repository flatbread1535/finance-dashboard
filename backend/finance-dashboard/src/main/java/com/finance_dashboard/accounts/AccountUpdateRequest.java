package com.finance_dashboard.accounts;

import org.hibernate.validator.constraints.URL;
import jakarta.validation.constraints.*;

public record AccountUpdateRequest(
    
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

    @URL(message = "Must be a valid URL.")
    @Size(max = 500, message = "Profile picture URL cannot be more than 500 characters.")
    String profilePictureUrl,

    @NotNull(message = "Dark mode toggle cannot be null.")
    Boolean darkModeEnabled

) {}
