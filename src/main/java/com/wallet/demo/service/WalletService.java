package com.wallet.demo.service;

import com.wallet.demo.model.Account;
import com.wallet.demo.model.Transaction;

import java.util.Set;

public interface WalletService {
    public int addMoneyToWallet(Account account, String username) throws Exception;
    public void sendMoneyToUser(Transaction transaction, String username) throws Exception;
    public Set<Transaction> getUserTransactions(String username) throws Exception;
}
