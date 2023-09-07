package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.LoanService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class LoanServiceImplement implements LoanService {
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Override
    public ResponseEntity<Object> getAllLoans() {
        return new ResponseEntity<>(loanRepository.findAll().stream().map(LoanDTO::new).collect(toList()), HttpStatus.ACCEPTED);
    }

    @Transactional
    public ResponseEntity<Object> applyLoanToClient(Authentication authentication, LoanApplicationDTO loanApplicationDTO) {
        // --------- Verifications ----------
        // Verify that the client exists
        Client authenticatedClient = clientRepository.findByEmail(authentication.getName()).orElse(null);
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
        Loan loanType = loanRepository.findById(loanApplicationDTO.getLoanId()).orElse(null);
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
        Account destinationAccount = accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber());
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
            accountRepository.save(destinationAccount);
            // 7° Save transaction
            transactionRepository.save(creditTransaction);
            // 8° Save client loand
            clientLoanRepository.save(newLoan);

            return new ResponseEntity<>("Loan created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("You can have only one loan per type", HttpStatus.FORBIDDEN);
        }
    }
}
