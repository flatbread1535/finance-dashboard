package com.finance_dashboard.accounts;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "accounts")
public class Account {

    @Id
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
}
