package com.finance_dashboard.transactions;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TransactionRequest(
    
    @NotNull(message = "Transaction amount cannot be null.")
    BigDecimal amount,

    @NotNull(message = "Currency status cannot be null.")
    Currency currency,

    @NotNull(message = "Transaction status cannot be null.")
    Status status,

    @NotBlank(message = "Transaction category cannot be blank.")
    @Size(max = 50, message = "Category must be no more than 50 characters.")
    String category

) {}
