package com.financedashboard.transactions;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data transfer object representing a response to get a transaction.
 *
 * @param transactionId the transaction primary key identifier
 * @param timeCreated the time the transaction was created
 * @param amount the transaction ammount
 * @param currency the currency used for the transaction
 * @param status the status of the transaction
 * @param category the category of the transaction
 */
public record TransactionResponse(
    Long transactionId, 
    LocalDateTime timeCreated, 
    BigDecimal amount,
    Currency currency, 
    Status status, 
    String category
) {}
