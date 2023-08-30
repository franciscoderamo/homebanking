package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.getAccounts();
    }
    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return accountService.getAccount(id);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        return accountService.createAccount(authentication);
    }

    @RequestMapping("/clients/current/accounts")
    public List<AccountDTO> getClientAccount(Authentication authentication){
        return accountService.getClientAccount(authentication);
    }

    public String createRandomNumberAccount(int min, int max) {
        return accountService.createRandomNumberAccount(min,max);
    }

}
