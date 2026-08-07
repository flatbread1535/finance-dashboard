package com.financedashboard.accounts;

import java.time.LocalDateTime;

/**
 * Data transfer object representing a response to get an account.
 *
 * @param accountId primary key of the account
 * @param role the account's role
 * @param username the unique account username
 * @param email the unique account email address
 * @param phoneNumber the unique account phone number
 * @param profilePictureUrl a URL for the account's profile picture
 * @param darkModeEnabled if dark mode is enabled
 * @param timeCreated timestamp at which the account was created
 * @param lastLoginTime timestamp at which the account was last logged in
 */
public record AccountResponse(
    Long accountId, 
    Role role, 
    String username, 
    String email,            
    String phoneNumber, 
    String profilePictureUrl, 
    Boolean darkModeEnabled,       
    LocalDateTime timeCreated, 
    LocalDateTime lastLoginTime
) {}
