package com.finance_dashboard.authentication;

public record LoginResponseDTO(
    Long accountId,
    String username
) {}
