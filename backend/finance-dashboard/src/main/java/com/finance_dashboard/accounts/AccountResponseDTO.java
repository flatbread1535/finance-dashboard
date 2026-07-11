package com.finance_dashboard.accounts;

public record AccountResponseDTO(
    Long accountId,
    String username,
    String email,
    String phoneNumber
) {}
