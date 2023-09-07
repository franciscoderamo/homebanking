package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.TransactionService;
import com.mindhub.homebanking.utilities.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService accountService;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> register(Authentication authentication, @RequestParam double amount, @RequestParam String fromAccountNumber, @RequestParam String toAccountNumber, @RequestParam String description) {
        // ------------- Verifications ---------------
        // Verify that the parameters are not empty
        if (!NumberUtils.isValidAmount(amount) || fromAccountNumber.isEmpty() || toAccountNumber.isEmpty() || description.isEmpty()) {
            String errorMessage = "Empty or invalid data: ";
            List<String> missingFields = new ArrayList<>();

            if (!NumberUtils.isValidAmount(amount)) {
                missingFields.add("amount");
            }
            if (fromAccountNumber.isEmpty()) {
                missingFields.add("from account number");
            }
            if (toAccountNumber.isEmpty()) {
                missingFields.add("to account number");
            }
            if (description.isEmpty()) {
                missingFields.add("description");
            }

            errorMessage += String.join(", ", missingFields);
            return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);

        }

        // Verify that the account numbers are not the same
        if(fromAccountNumber.equals(toAccountNumber)){
            return new ResponseEntity<>("Account numbers must be different", HttpStatus.FORBIDDEN);
        }

        //Verify that the source account and destination account exist
        Account sourceAccount = accountService.getAccountByNumber(fromAccountNumber);
        Account destinationAccount= accountService.getAccountByNumber(toAccountNumber);
        if(sourceAccount == null || destinationAccount == null){
            return new ResponseEntity<>("Source account or destination account not found",HttpStatus.FORBIDDEN);
        }

        //Verify that the source account belongs to the authenticated client
        String emailClientSourceAccount = sourceAccount.getClient().getEmail();
        if(!emailClientSourceAccount.equals(authentication.getName())){
            return new ResponseEntity<>("Invalid source account. Verify that the source account is yours",HttpStatus.FORBIDDEN);
        }
        //Verify that the source account has the amount available.
        if(sourceAccount.getBalance() < amount){
            return new ResponseEntity<>("Insufficient funds",HttpStatus.FORBIDDEN);
        }

        // ------------- Transaction ---------------

        // Create "DEBIT" transaction associated with the source account
        Transaction debitTransaction = new Transaction(TransactionType.DEBIT,-amount, LocalDateTime.now(), description);
        sourceAccount.addTransaction(debitTransaction);

        // Create "CREDIT" transaction associated with the destination account TransactionType type, double amount, LocalDateTime date, String description
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT, amount, LocalDateTime.now(), description);
        destinationAccount.addTransaction(creditTransaction);

        // The amount indicated in the request will be subtracted from the source account
        sourceAccount.minusBalance(amount);

        // The same amount will be added to the destination account
        destinationAccount.plusBalance(amount);

        // Save the accounts
        accountService.saveAccount(sourceAccount);
        accountService.saveAccount(destinationAccount);

        //  Save the transactions
        transactionService.saveTransaction(debitTransaction);
        transactionService.saveTransaction(creditTransaction);
        return new ResponseEntity<>("Successful transaction", HttpStatus.CREATED);
    }
}