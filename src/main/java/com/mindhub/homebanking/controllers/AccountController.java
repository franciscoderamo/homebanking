package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccountsDTO(){
        return accountService.getAccountsDTO();
    }
    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return accountService.getAccount(id);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {//Get authenticated client information
        String clientEmail = authentication.getName(); //Get customer email
        Client client = clientService.findByEmail(clientEmail); //Search for the client according to the email of the authenticated user and save it
        if (accountService.findByClientEmail(clientEmail).size() < 3 && client.getEmail() != null) {// Check if the customer has less than 3 accounts
            Account account = new Account(createRandomNumberAccount(1, 99999999), LocalDate.now(), 0.0);// Account generated with random number, current date and amount $0.
            account.setClient(client); // The account is associated with the client with the session started
            accountService.saveAccount(account); //Save the account through the repository
            return new ResponseEntity<>("Account created successfully", HttpStatus.CREATED);// Return a “201 created” response
        } else {// If the customer already has 3 accounts
            return new ResponseEntity<>("You already have as many accounts as possible", HttpStatus.FORBIDDEN);// Return a “403 Forbidden” response
        }
    }

    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getClientAccount(Authentication authentication){
        //Verify that you meet all the conditions
        String clientEmail = authentication.getName(); //Get customer email
        List<Account> clientAccounts = accountService.findByClientEmail(clientEmail);
        List<AccountDTO> accountDTO = clientAccounts.stream()
                .map(AccountDTO::new)
                .collect(Collectors.toList());
        return accountDTO;
    }

    //Generate random numbers of accounts in a given range
    public String createRandomNumberAccount(int min, int max) {
        int number = (int) ((Math.random() * (max - min)) + min);
        return "VIN" + number;
    }

}