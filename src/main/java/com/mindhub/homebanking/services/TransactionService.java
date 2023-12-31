package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Transaction;

import java.util.Set;

public interface TransactionService {
    void saveTransaction(Transaction transaction);
    void deleteTransactions (Set<Transaction> transactions);
    void transactionsDisabled(Set<Transaction> transactions);
}