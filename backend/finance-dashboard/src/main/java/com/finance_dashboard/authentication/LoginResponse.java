package com.finance_dashboard.authentication;

import com.finance_dashboard.accounts.Role;

public record LoginResponse(
                Long accountId,
                Role role,
                String username,
                String token) {
}
