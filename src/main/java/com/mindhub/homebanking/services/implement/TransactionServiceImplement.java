package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TransactionServiceImplement implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(Transaction transaction){
        transactionRepository.save(transaction);
    }
    @Override
    public void deleteTransactions(Set<Transaction> transactions) {
        transactionRepository.deleteAll(transactions);
    }
    @Override
    public void transactionsDisabled(Set<Transaction> transactions) {
        for(Transaction transaction : transactions){
            transaction.setIsDisabled();
        }
    }
}