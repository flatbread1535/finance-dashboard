package com.finance_dashboard.accounts;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/accounts")
@PreAuthorize("hasRole('ADMIN')")
public class AdminAccountController {

    private AccountService accountService;

    public AdminAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long accountId) {
        return ResponseEntity.ok(accountService.getAccountById(accountId));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccountById(@PathVariable Long accountId) {
        accountService.deleteAccountById(accountId);
        return ResponseEntity.noContent().build();
    }
}
