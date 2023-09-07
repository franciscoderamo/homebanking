package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientLoanService clientLoanService;

    @RequestMapping("/loans")
    public ResponseEntity<Object> getAllLoans() {
        return new ResponseEntity<>(loanService.getAllLoansDTO(), HttpStatus.ACCEPTED);
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> applyLoanToClient(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO) {
        // --------- Verifications ----------
        // Verify that the client exists
        Client authenticatedClient = clientService.findByEmail(authentication.getName());
        if(authenticatedClient == null) {
            return new ResponseEntity<>("Client not found", HttpStatus.FORBIDDEN);
        }
        // Verify that the email is not empty
        if(authenticatedClient.getEmail().isBlank()) {
            return new ResponseEntity<>("Lost customer email", HttpStatus.FORBIDDEN);
        }
        // Verify that amount is not 0 or negative
        if(loanApplicationDTO.getAmount() <= 0) {
            return new ResponseEntity<>("The quantity of amount can't be zero.", HttpStatus.FORBIDDEN);
        }
        // Verify that payments is not 0
        if(loanApplicationDTO.getPayments() == 0) {
            return new ResponseEntity<>("Payments can't be zero", HttpStatus.FORBIDDEN);
        }
        // Check that the destination account exists
        if(loanApplicationDTO.getToAccountNumber().isBlank()) {
            return new ResponseEntity<>("Missing number account", HttpStatus.FORBIDDEN);
        }
        // Verify that the type of loan exists
        Loan loanType = loanService.getLoanById(loanApplicationDTO.getLoanId());
        if(loanType == null) {
            return new ResponseEntity<>("Loan type not found", HttpStatus.FORBIDDEN);
        }
        // Verify that you do not exceed the maximum loan amount
        if(loanType.getMaxAmount() < loanApplicationDTO.getAmount()) {
            return new ResponseEntity<>("Maximum amount reached. Request a lower amount", HttpStatus.FORBIDDEN);
        }
        // Validate amount of chosen payments
        if(!loanType.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("Payments incorrect", HttpStatus.FORBIDDEN);
        }
        // Check that the destination account exists
        Account destinationAccount = accountService.getAccountByNumber(loanApplicationDTO.getToAccountNumber());
        if(destinationAccount==null) {
            return new ResponseEntity<>("Destination account not found", HttpStatus.FORBIDDEN);
        }
        // Verify that the destination account belongs to the client
        if(!authenticatedClient.getAccounts().contains(destinationAccount)) {
            return new ResponseEntity<>("The account does not belong to the current client", HttpStatus.FORBIDDEN);
        }

        // --------- Loan application --------
        // Check loanType limits
        Set<ClientLoan> clientLoans = authenticatedClient.getClientLoans();
        int loansType = (int) clientLoans.stream().filter(loan -> loan.getLoan() == loanType).count();

        if (loansType == 0) {
            // 1° Create new loan
            ClientLoan newLoan = new ClientLoan(authenticatedClient, loanType, loanApplicationDTO.getPayments(), loanApplicationDTO.getAmount() * 1.2);
            // 2° Associate client-loan
            authenticatedClient.addClientLoan(newLoan);
            loanType.addClientLoan(newLoan);
            // 3° Create credit type transaction
            Transaction creditTransaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), LocalDateTime.now(), loanType.getName() + " loan approved");
            // 4° Associate transaction to customer
            destinationAccount.addTransaction(creditTransaction);
            // 5° Add amount to account
            destinationAccount.plusBalance(loanApplicationDTO.getAmount());
            // 6° Save account
            accountService.saveAccount(destinationAccount);
            // 7° Save transaction
            transactionService.saveTransaction(creditTransaction);
            // 8° Save client loand
            clientLoanService.saveClientLoan(newLoan);

            return new ResponseEntity<>("Loan created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("You can have only one loan per type", HttpStatus.FORBIDDEN);
        }
    }

}
