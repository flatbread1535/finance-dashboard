package com.finance_dashboard.accounts;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
         super(message);
    }
}
