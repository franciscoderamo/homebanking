package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface AccountService {
//    public List<AccountDTO> getAccounts();
//    public AccountDTO getAccount(Long id);
//
//    public ResponseEntity<Object> createAccount(Authentication authentication);
//
//    public List<AccountDTO> getClientAccount(Authentication authentication);
//    public String createRandomNumberAccount(int min, int max);

    List<AccountDTO> getAccountsDTO();

    AccountDTO getAccount(Long id);

    Account getAccountByNumber(String number);

    void saveAccount(Account account);

    Account getAccountById(long id);

    AccountDTO getCurrentClient(String email);

    List<Account> findByClientEmail(String email);

    ResponseEntity<Object> createAccount(Authentication authentication);

    List<AccountDTO> getClientAccount(Authentication authentication);
}