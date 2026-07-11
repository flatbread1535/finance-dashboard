package com.finance_dashboard.accounts;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
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

    // Maps incoming HTTP GET requests to method
    @GetMapping("/{accountId}")
    // Returns entire HTTP response, data sent back to client is serialized Account
    // object
    public ResponseEntity<AccountResponseDTO> findByAccountId(@PathVariable Long accountId) {
        return accountService.getAccountById(accountId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    // @RequestBody tells SpringBoot to look at body of HTTP request, grab raw JSON,
    // and convert to Java object
    public ResponseEntity<Void> createAccount(@RequestBody @Valid AccountRequestDTO accountRequest,
            UriComponentsBuilder ucb) {
        Account savedAccount = accountService.createAccount(accountRequest);
        // Builds account path structure into a Java URI object
        URI locationOfNewAccount = ucb.path("/accounts/{accountId}")
                .buildAndExpand(savedAccount.getAccountId()).toUri();
        return ResponseEntity.created(locationOfNewAccount).build();
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<Void> putAccount(@PathVariable Long accountId,
            @RequestBody @Valid AccountRequestDTO updateRequest) {
        accountService.updateAccount(accountId, updateRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }
}
