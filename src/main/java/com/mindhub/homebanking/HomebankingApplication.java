package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
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

			clientRepository.save(client1);
			clientRepository.save(client2);

			//Establishing relationships
			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);
			//Save accounts
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			//Save clients again
			clientRepository.save(client1);
			clientRepository.save(client2);

			//Establishing relationships
			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account2.addTransaction(transaction3);
			account2.addTransaction(transaction4);
			account3.addTransaction(transaction5);
			account3.addTransaction(transaction6);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
		});
	}

}
