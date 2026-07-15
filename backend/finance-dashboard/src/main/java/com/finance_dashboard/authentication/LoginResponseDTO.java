package com.finance_dashboard.authentication;

import com.finance_dashboard.accounts.Role;

public record LoginResponseDTO(
                Long accountId,
                Role role,
                String username,
                String token) {
}
