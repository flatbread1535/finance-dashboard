package com.financedashboard.authentication;

import com.financedashboard.accounts.Role;

/**
 * Data transfer object representing a successful authentication response.
 *
 * @param accountId the unique primary key identifier of the authenticated account
 * @param role the user's role
 * @param username the unique username given by the user
 * @param token the JWT used to authorize API requests
 */
public record LoginResponse(
    Long accountId, 
    Role role, 
    String username, 
    String token
) {}
