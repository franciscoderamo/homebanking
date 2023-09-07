package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @RequestMapping("/loans")
    public ResponseEntity<Object> getLoans(){
        return loanService.getAllLoans();
    }

    @PostMapping("/loans")
    public ResponseEntity<Object> addLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO){
        if(authentication == null) {
            return new ResponseEntity<>("Unauthenticated user", HttpStatus.FORBIDDEN);
        }
        return loanService.applyLoanToClient(authentication,loanApplicationDTO);
    }

}
