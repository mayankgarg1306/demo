package com.wallet.demo.serviceImpl;

import com.wallet.demo.model.Account;
import com.wallet.demo.model.Transaction;
import com.wallet.demo.Entity.AccountEntity;
import com.wallet.demo.Entity.TransactionEntity;
import com.wallet.demo.Entity.UserCredentialEntity;
import com.wallet.demo.Entity.UserEntity;
import com.wallet.demo.constants.Constants;
import com.wallet.demo.repository.TransactionRepository;
import com.wallet.demo.repository.UserCredentialRepository;
import com.wallet.demo.repository.UserRepository;
import com.wallet.demo.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class WalletServiceImpl implements WalletService {

    @Autowired
    UserCredentialRepository userCredentialRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public int addMoneyToWallet(Account account, String username) throws Exception {
        try {
            Optional<UserCredentialEntity> userCredentialEntityOptional = userCredentialRepository.findByUsername(username);
            if (userCredentialEntityOptional.isPresent()) {
                UserCredentialEntity userCredentialEntity = userCredentialEntityOptional.get();
                UserEntity userEntity = userCredentialEntity.getUserEntity();
                AccountEntity accountEntity = userCredentialEntityOptional.get().getUserEntity().getAccountEntity();
                accountEntity.setAmount(accountEntity.getAmount() + account.getAmount());
                return accountEntity.getAmount();
            } else {
                throw new Exception("User not present with Username:" + username);
            }
        }catch (Exception e){
            throw new Exception("Not Able to add Money");
        }
    }

    @Override
    public void sendMoneyToUser(Transaction transaction, String username) throws Exception {
        UserEntity fromUserEntity;
        UserEntity toUserEntity=null;
        String status= Constants.failed;
        Boolean toUserPresent=false;
        Boolean sufficientBalanceAvail=false;
        if(username.equals(transaction.getToUser()))
            throw new Exception("Can not send money to yourself");
        try{
            Optional<UserCredentialEntity> fromUserCredentialEntityOptional=userCredentialRepository.findByUsername(username);
            if(fromUserCredentialEntityOptional.isPresent()){

                UserCredentialEntity fromUserCredentialEntity = fromUserCredentialEntityOptional.get();
                fromUserEntity = fromUserCredentialEntity.getUserEntity();
                AccountEntity accountEntity = fromUserEntity.getAccountEntity();
                if(accountEntity.getAmount()>= transaction.getAmount()){
                    sufficientBalanceAvail=true;
                }
                Optional<UserCredentialEntity> toUserCredentialEntityOptional=userCredentialRepository.findByUsername(transaction.getToUser());
                if(toUserCredentialEntityOptional.isPresent()) {
                    toUserPresent=true;
                    UserCredentialEntity toUserCredentialEntity = toUserCredentialEntityOptional.get();
                    toUserEntity = toUserCredentialEntity.getUserEntity();
                    if(sufficientBalanceAvail) {
                        AccountEntity toAccountEntity = toUserEntity.getAccountEntity();
                        accountEntity.setAmount(accountEntity.getAmount() - transaction.getAmount());
                        toAccountEntity.setAmount(toAccountEntity.getAmount() + transaction.getAmount());
                        status = Constants.success;
                        //addingTransactionEntry(fromUserEntity, toUserEntity, transactionDTO.getAmount(), status);
                    }
                }
                addingTransactionEntry(fromUserEntity, toUserEntity, transaction.getAmount(), status);
                if(!sufficientBalanceAvail){
                    throw new Exception("Insufficient Balance");
                }else if(!toUserPresent){
                    throw new Exception("User is In-Valid");
                }
            }
        }catch(Exception e){
            throw new Exception("Transaction Aborted");
        }
    }

    private void addingTransactionEntry(UserEntity fromUser, UserEntity toUser, int amount, String status){
        TransactionEntity transactionEntity=populateTransactionEntity(fromUser, toUser, amount, status);
        transactionRepository.save(transactionEntity);
    }

    @Override
    public Set<Transaction> getUserTransactions(String username) throws Exception {
        Optional<UserCredentialEntity> fromUserCredentialEntityOptional;
        UserEntity userEntity;
        Set<Transaction> transactionSet=new HashSet<>();
        try {
            //finding debited transactions
            fromUserCredentialEntityOptional = userCredentialRepository.findByUsername(username);
            if (fromUserCredentialEntityOptional.isPresent()) {
                UserCredentialEntity fromUserCredentialEntity = fromUserCredentialEntityOptional.get();
                userEntity = fromUserCredentialEntity.getUserEntity();
                Set<TransactionEntity> debitedTransactionEntitySet=userEntity.getTransactionEntitySet();
                Set<TransactionEntity> creditedTransactionEntitySet=userEntity.getToUserTransactionEntitySet();
                for(TransactionEntity transactionEntity:debitedTransactionEntitySet){
                    Transaction transaction=populateTransaction(transactionEntity, Constants.debited);
                    transactionSet.add(transaction);
                }
                for(TransactionEntity transactionEntity:creditedTransactionEntitySet){
                    Transaction transaction=populateTransaction(transactionEntity, Constants.credited);
                    transactionSet.add(transaction);
                }
            }else{
                throw new Exception("User Not Found");
            }

            return transactionSet;
        }catch (Exception e){
            throw new Exception("Not able to Fetch Transaction");
        }
    }

    private TransactionEntity populateTransactionEntity(UserEntity fromUser, UserEntity toUser, int amount, String status){
        TransactionEntity transactionEntity=new TransactionEntity();
        transactionEntity.setAmount(amount);
        transactionEntity.setFromUser(fromUser);
        transactionEntity.setToUser(toUser);
        transactionEntity.setStatus(status);
        transactionEntity.setTimestamp(new Date());
        return transactionEntity;
    }

    private Transaction populateTransaction(TransactionEntity transactionEntity, String type){
        Transaction transaction=new Transaction();
        UserEntity fromUserEntity=transactionEntity.getFromUser();
        UserEntity toUserEntity=transactionEntity.getToUser();
        transaction.setAmount(transactionEntity.getAmount());
        transaction.setDate(transactionEntity.getTimestamp());
        transaction.setFromUser(fromUserEntity==null?Constants.invalidUser:fromUserEntity.getName());
        transaction.setToUser(toUserEntity==null?Constants.invalidUser:toUserEntity.getName());
        transaction.setId(transactionEntity.getTransactionId());
        transaction.setType(type);
        transaction.setStatus(transactionEntity.getStatus());
        return transaction;
    }
}
