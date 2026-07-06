CREATE TABLE accounts (
    account_id BIGINT PRIMARY KEY,
    username VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    hash_password VARCHAR(250) NOT NULL,
    phone_number VARCHAR(20) NOT NULL UNIQUE
);
