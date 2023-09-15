package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTest {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    TransactionRepository transactionRepository;


    // Test on AccountRepository
    @Test
    public void existAccount(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts,is(not(empty())));
    }
    @Test
    public void existNumberAccountClient(){
        List<Account> accounts = accountRepository.findByClientId(1L);
        assertThat(accounts, hasItem(hasProperty("number",is("VIN001"))));
    }

    // Test on CardRepository
    @Test
    public void noDuplicateCardTypesAndColorsForClients() {
        List<Client> clients = clientRepository.findAll();
        for (Client client : clients) {
            List<Card> cards = cardRepository.findByClientEmail(client.getEmail());
            Map<String, Long> cardTypeAndColorCounts = cards.stream()
                    .collect(Collectors.groupingBy(
                            card -> card.getType().toString() + "-" + card.getColor().toString(),
                            Collectors.counting()
                    ));

            for (Map.Entry<String, Long> entry : cardTypeAndColorCounts.entrySet()) {
                assertEquals(1, entry.getValue(), "The client " + client.getFirstName() + " " + client.getLastName() +
                        " has more than one card of the same type and color: " + entry.getKey());
            }
        }
    }

    @Test
    public void hasCard(){
        List<Card> cards = cardRepository.findByClientId(1L);
        assertThat(cards, hasItem(hasProperty("cardHolder",is("Morel Melba"))));
    }


    // Test on ClientRepository
    @Test
    public void existUserAdmin(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, hasItem(hasProperty("firstName", is("admin"))));
    }

    @Test
    public void findClientsByProperty() {
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, hasItem(hasProperty("email", is("admin@correo.com"))));
    }

    // Test on LoanRepository
    @Test
    public void existLoans(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }

    @Test
    public void existPersonalLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }

    // Test on TransactionRepository
    @Test
    public void existCreditTransaction(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, hasItem(hasProperty("amount", lessThan(0.0)))); // 0.0 is used to format the value of type double
    }
    @Test
    public void existDebitTransaction(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, hasItem(hasProperty("amount", greaterThan(0.0)))); // 0.0 is used to format the value of type double
    }
}