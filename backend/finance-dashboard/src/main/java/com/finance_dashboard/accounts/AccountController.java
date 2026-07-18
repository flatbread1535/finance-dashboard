package com.finance_dashboard.accounts;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.util.UriComponentsBuilder;
import jakarta.validation.Valid;

import java.net.URI;

// Declares class as controller to Spring
@RestController
// Indicates which address requests must have to access this controller
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    // Injects Account service into Controller
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/me")
    public ResponseEntity<AccountResponse> getAccount(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(accountService.getAccountByUsername(username));
    }

    @PostMapping
    // @RequestBody tells SpringBoot to look at body of HTTP request, grab raw JSON,
    // and convert to Java object
    public ResponseEntity<Void> createAccount(@RequestBody @Valid AccountCreateRequest accountRequest,
            UriComponentsBuilder ucb) {
        Account savedAccount = accountService.createAccount(accountRequest);
        // Builds account path structure into a Java URI object
        URI locationOfNewAccount = ucb.path("/accounts/{accountId}")
                .buildAndExpand(savedAccount.getAccountId()).toUri();
        return ResponseEntity.created(locationOfNewAccount).build();
    }

    @PutMapping("/me")
    public ResponseEntity<Void> updateAccount(Authentication authentication,
            @RequestBody @Valid AccountUpdateRequest updateRequest) {
        String username = authentication.getName();
        accountService.updateAccount(username, updateRequest);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> updatePassword(Authentication authentication,
            @RequestBody @Valid PasswordChangeRequest passwordChangeRequest) {
        accountService.updatePassword(authentication.getName(), passwordChangeRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteAccount(Authentication authentication) {
        String username = authentication.getName();
        accountService.deleteAccount(username);
        return ResponseEntity.noContent().build();
    }
}
