package com.finance_dashboard.accounts;

import jakarta.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "username", nullable = false, unique = true, length = 20)
    private String username;

    @Column(name = "email", nullable =false, unique = true, length = 100)
    private String email;

    @Column(name = "hash_password", nullable = false, length = 250)
    private String hashPassword;

    @Column(name = "phone_number", nullable = false, unique = true, length = 20)
    private String phoneNumber;

    protected Account() {}

    public Account(
        Long accountId,
        String username,
        String email,
        String hashPassword,
        String phoneNumber
    ) {
        this.accountId = accountId;
        this.username = username;
        this.email = email;
        this.hashPassword = hashPassword;
        this.phoneNumber = phoneNumber;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
