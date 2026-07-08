package com.finance_dashboard.transactions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> findById(@PathVariable Long transactionId) {
        Optional<Transaction> transactionOptional = transactionService.getTransactionById(transactionId);
        if (transactionOptional.isPresent()) {
            return ResponseEntity.ok(transactionOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Iterable<Transaction>> findAll() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @PostMapping
    public ResponseEntity<Void> createTransaction(@RequestBody TransactionRequest request) {
        Transaction requestedTransaction = transactionService.createTransaction(request);
        URI location = URI.create("/transactions/" + requestedTransaction.getTransactionId());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<Void> putTransaction(@PathVariable Long transactionId, @RequestBody TransactionRequest updateRequest) {
        try {
            transactionService.updateTransaction(transactionId, updateRequest);
            return ResponseEntity.noContent().build();
        } catch (TransactionNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long transactionId) {
        try {
            transactionService.deleteTransaction(transactionId);
            return ResponseEntity.noContent().build();
        } catch (TransactionNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
