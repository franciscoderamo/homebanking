package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class AccountServiceImplement implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Override
    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }
    @Override
    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    @Override
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {//Get authenticated client information
        String clientEmail = authentication.getName(); //Get customer email
        Client client = clientRepository.findByEmail(clientEmail).orElse(null); //Search for the client according to the email of the authenticated user and save it
        if (accountRepository.findByClientEmail(clientEmail).size() < 3) {// Check if the customer has less than 3 accounts
            Account account = new Account(createRandomNumberAccount(1, 99999999), LocalDate.now(), 0.0);// Account generated with random number, current date and amount $0.
            account.setClient(client); // The account is associated with the client with the session started
            accountRepository.save(account); //Save the account through the repository
            return new ResponseEntity<>("Account created successfully", HttpStatus.CREATED);// Return a “201 created” response
        } else {// If the customer already has 3 accounts
            return new ResponseEntity<>("You already have as many accounts as possible", HttpStatus.FORBIDDEN);// Return a “403 Forbidden” response
        }
    }

    @Override
    @RequestMapping("/clients/current/accounts")
    public List<AccountDTO> getClientAccount(Authentication authentication){
        String clientEmail = authentication.getName(); //Get customer email
        List<Account> clientAccounts = accountRepository.findByClientEmail(clientEmail);
        List<AccountDTO> accountDTO = clientAccounts.stream()
                .map(AccountDTO::new)
                .collect(Collectors.toList());
        return accountDTO;
    }

    //Generate random numbers of accounts in a given range
    @Override
    public String createRandomNumberAccount(int min, int max) {
        int number = (int) ((Math.random() * (max - min)) + min);
        return "VIN" + number;
    }
}