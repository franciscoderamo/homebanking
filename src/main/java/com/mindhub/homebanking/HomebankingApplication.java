package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository) {
		return (args -> {
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
			Client client2 = new Client("Francisco", "DEramo", "franciscoderamo@gmail.com");
			// First we save the clients in the DB so that they have an ID assigned and only there we can add the accounts

			Account account1 = new Account("VIN001", LocalDate.now(), 5000.0);
			Account account2 = new Account("VIN002", LocalDate.now().plusDays(1), 7500.0);
			Account account3 = new Account("VIN003", LocalDate.now().plusDays(2), 500000.0);

			Transaction transaction1 = new Transaction(TransactionType.DEBIT, -500.0, LocalDateTime.now(),"SUPERMARKET");
			Transaction transaction2 = new Transaction(TransactionType.CREDIT, 6000.0, LocalDateTime.now(),"SHOPPING");
			Transaction transaction3 = new Transaction(TransactionType.CREDIT, 300.0, LocalDateTime.now(),"BAKERY");
			Transaction transaction4 = new Transaction(TransactionType.DEBIT, -2800.0, LocalDateTime.now(),"EDESUR");
			Transaction transaction5 = new Transaction(TransactionType.DEBIT, -3000.0, LocalDateTime.now(),"FARMACY");
			Transaction transaction6 = new Transaction(TransactionType.CREDIT, 7800.0, LocalDateTime.now(),"METROGAS");

			//Creating payments
			List<Integer> payments1 = Arrays.asList(12, 24, 36, 48, 60);
			List<Integer> payments2 = Arrays.asList(6, 12, 24);
			List<Integer> payments3 = Arrays.asList(6, 12, 24, 36);

			//Creating loan
			Loan loan1 = new Loan("Mortgage",500000,payments1);
			Loan loan2 = new Loan("Personal",100000,payments2);
			Loan loan3 = new Loan("Automotive",300000,payments3);

			// Create ClientLoan
			ClientLoan clientLoan1 = new ClientLoan(400000,60);
			ClientLoan clientLoan2 = new ClientLoan(50000,12);
			ClientLoan clientLoan3 = new ClientLoan(100000,24);
			ClientLoan clientLoan4 = new ClientLoan(200000,36);

			//Save clients
			clientRepository.save(client1);
			clientRepository.save(client2);

			//Saving accounts
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			//Saving clients again
			clientRepository.save(client1);
			clientRepository.save(client2);

			//Saving Loans
			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			//Saving ClientLoans
			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			//Establishing relationships Account - Transaction
			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account2.addTransaction(transaction3);
			account2.addTransaction(transaction4);
			account3.addTransaction(transaction5);
			account3.addTransaction(transaction6);

			//Establishing relationships Client - Account
			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);

			//Establishing relationships Loan - ClientLoan

			loan1.addClientLoan(clientLoan1);
			loan2.addClientLoan(clientLoan2);
			loan2.addClientLoan(clientLoan3);
			loan3.addClientLoan(clientLoan4);

			//Establishing relationships Client - ClientLoan

			client1.addClientLoan(clientLoan1);
			client1.addClientLoan(clientLoan2);
			client2.addClientLoan(clientLoan3);
			client2.addClientLoan(clientLoan4);

			//Saving Transaction
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);

			//Saving Account
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);

			//Saving clients
			clientRepository.save(client1);
			clientRepository.save(client2);

			//Saving accounts
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);

			//Saving ClientLoans
			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			//Saving Loans
			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

		});
	}

}
