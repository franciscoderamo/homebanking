package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.services.implement.TransactionServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionServiceImplement transactionService;

    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(Authentication authentication, @RequestParam double amount, @RequestParam String fromAccountNumber, @RequestParam String toAccountNumber, @RequestParam String description){
        return transactionService.register(authentication.getName(), amount, fromAccountNumber, toAccountNumber, description);
    }
}