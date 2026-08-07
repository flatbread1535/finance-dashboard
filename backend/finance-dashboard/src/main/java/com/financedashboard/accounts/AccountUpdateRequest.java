package com.financedashboard.accounts;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

/**
 * Data transfer object representing a request to create a new account.
 *
 * @param username the unique account username
 * @param email the unique account email address
 * @param phoneNumber the unique account phone number
 * @param profilePictureUrl a URL for the account's profile picture
 * @param darkModeEnabled if dark mode is enabled
 */
public record AccountUpdateRequest(

    @NotBlank(message = "Username cannot be blank.") 
    @Size(max = 20, message = "Username cannot be more than 20 characters.")
    String username,

    @NotBlank(message = "Email cannot be blank.") 
    @Size(max = 100, message = "Email cannot be more than 100 characters.") 
    @Email(message = "Invalid email format.") 
    String email,

    @NotBlank(message = "Phone number cannot be blank.") 
    @Size(max = 20, message = "Phone number cannot be more than 20 characters.") 
    String phoneNumber,

    @URL(message = "Must be a valid URL.") 
    @Size(max = 500, message = "Profile picture URL cannot be more than 500 characters.") 
    String profilePictureUrl,

    @NotNull(message = "Dark mode toggle cannot be null.") 
    Boolean darkModeEnabled

) {}
