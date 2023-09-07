package com.mindhub.homebanking.services;

import org.springframework.http.ResponseEntity;

public interface TransactionService {
    public ResponseEntity<Object> register(String clientEmail, double amount, String fromAccountNumber, String toAccountNumber, String description);
}