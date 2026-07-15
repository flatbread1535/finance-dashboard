package com.finance_dashboard.accounts;

import java.time.LocalDateTime;

public record AccountResponseDTO(
                Long accountId,
                Role role,
                String username,
                String email,
                String phoneNumber,
                String profilePictureUrl,
                Boolean darkModeEnabled,
                LocalDateTime timeCreated,
                LocalDateTime lastLoginTime) {
}
