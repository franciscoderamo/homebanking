package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface LoanService {
    public ResponseEntity<Object> getAllLoans();
    public ResponseEntity<Object> applyLoanToClient(Authentication authentication, LoanApplicationDTO loanApplicationDTO);

}
