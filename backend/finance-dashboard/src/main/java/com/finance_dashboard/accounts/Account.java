package com.finance_dashboard.accounts;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.URL;

import jakarta.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private Role role = Role.USER;

    @Column(name = "username", nullable = false, unique = true, length = 20)
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true, length = 20)
    private String phoneNumber;

    @URL(message = "Must be a valid URL.")
    @Column(name = "profile_picture_url", length = 500)
    private String profilePictureUrl;

    @Column(name = "dark_mode_enabled", nullable = false)
    private Boolean darkModeEnabled = false;

    @CreationTimestamp
    @Column(name = "time_created", nullable = false)
    private LocalDateTime timeCreated;

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    @Column(name = "hash_password", nullable = false, length = 250)
    private String hashPassword;

    protected Account() {
    }

    public Account(
            Long accountId,
            Role role,
            String username,
            String email,
            String phoneNumber,
            String profilePictureUrl,
            Boolean darkModeEnabled,
            LocalDateTime timeCreated,
            LocalDateTime lastLoginTime,
            String hashPassword) {
        this.accountId = accountId;
        this.role = role != null ? role : Role.USER;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profilePictureUrl = profilePictureUrl;
        this.darkModeEnabled = darkModeEnabled != null ? darkModeEnabled : false;
        this.timeCreated = timeCreated;
        this.lastLoginTime = lastLoginTime;
        this.hashPassword = hashPassword;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public Boolean getDarkModeEnabled() {
        return darkModeEnabled;
    }

    public void setDarkModeEnabled(Boolean darkModeEnabled) {
        this.darkModeEnabled = darkModeEnabled;
    }

    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(LocalDateTime timeCreated) {
        this.timeCreated = timeCreated;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }
}
