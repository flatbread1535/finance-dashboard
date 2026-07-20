package com.finance_dashboard.transactions;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransaction(@PathVariable Long transactionId, Authentication authentication) {
        return ResponseEntity.ok(transactionService.getTransaction(transactionId, authentication.getName()));
    }

    @GetMapping
    public ResponseEntity<Page<TransactionResponse>> getAllTransactions(Pageable pageable, Authentication authentication) {
        return ResponseEntity.ok(transactionService.getTransactions(pageable, authentication.getName()));
    }

    @PostMapping
    public ResponseEntity<Void> createTransaction(Authentication authentication, @RequestBody @Valid TransactionRequest request) {
        String username = authentication.getName();
        Transaction requestedTransaction = transactionService.createTransaction(username, request);
        URI location = URI.create("/transactions/" + requestedTransaction.getTransactionId());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<Void> updateTransaction(@PathVariable Long transactionId, Authentication authentication, @RequestBody @Valid TransactionRequest updateRequest) {
        transactionService.updateTransaction(transactionId, authentication.getName(), updateRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long transactionId, Authentication authentication) {
            transactionService.deleteTransaction(transactionId, authentication.getName());
            return ResponseEntity.noContent().build();
    }
}
