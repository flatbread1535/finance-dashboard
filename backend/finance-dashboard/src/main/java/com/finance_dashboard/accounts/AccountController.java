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

import java.net.URI;
import java.util.Optional;

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
    @GetMapping("/{requestedAccountId}")
    // Returns entire HTTP response, data sent back to client is serialized Account
    // object
    public ResponseEntity<Account> findByAccountId(@PathVariable Long requestedAccountId) {
        Optional<Account> accountOptional = accountService.getAccountById(requestedAccountId);
        if (accountOptional.isPresent()) {
            // Returns successful request (200 OK)
            return ResponseEntity.ok(accountOptional.get());
        } else {
            // Generates empty HTTP response with 404 NOT_FOUND status code
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping()
    public ResponseEntity<Iterable<Account>> findAll() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @PostMapping
    // @RequestBody tells SpringBoot to look at body of HTTP request, grab raw JSON,
    // and convert to Java object
    public ResponseEntity<Void> createAccount(@RequestBody AccountRegistrationRequest newAccountRequest, UriComponentsBuilder ucb) {
        Account savedAccount = accountService.createAccount(newAccountRequest);
        // Builds account path structure into a Java URI object
        URI locationOfNewAccount = ucb.path("/accounts/{accountId}")
                .buildAndExpand(savedAccount.getAccountId()).toUri();
        return ResponseEntity.created(locationOfNewAccount).build();
    }

    @PutMapping("/{requestedAccountId}")
    public ResponseEntity<Void> putAccount(@PathVariable Long requestedAccountId, @RequestBody AccountRegistrationRequest accountUpdateRequest) {
        try {
            accountService.updateAccount(requestedAccountId, accountUpdateRequest);
            return ResponseEntity.noContent().build();
        } catch (AccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{requestedAccountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long requestedAccountId) {
        try {
            accountService.deleteAccount(requestedAccountId);
            return ResponseEntity.noContent().build();
        } catch (AccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
