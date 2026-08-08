package com.financedashboard.transactions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Data transfer object representing a request to create a 
 * new transaction.
 *
 * @param amount the transaction amount provided
 * @param currency the type of currency used in the transaction
 * @param status the provided status of the transaction
 * @param category the provided category of the transaction
 */
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
