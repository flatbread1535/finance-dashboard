CREATE TABLE transactions(
    transaction_id BIGINT PRIMARY KEY,
    time_created DATETIME2 NOT NULL DEFAULT(SYSDATETIME()),
    account_id BIGINT NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(20) NOT NULL,
    category VARCHAR(50),
    CONSTRAINT FK_transactions_accounts 
    FOREIGN KEY (account_id) 
    REFERENCES accounts(account_id)
);
