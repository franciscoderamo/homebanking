package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import java.util.List;

public interface AccountService {
    public List<AccountDTO> getAccounts();
    public AccountDTO getAccount(Long id);

    public ResponseEntity<Object> createAccount(Authentication authentication);

    public List<AccountDTO> getClientAccount(Authentication authentication);
    public String createRandomNumberAccount(int min, int max);
}