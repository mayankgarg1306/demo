package com.wallet.demo.controller;

import com.wallet.demo.model.Account;
import com.wallet.demo.model.Transaction;
import com.wallet.demo.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    WalletService walletService;

    @PutMapping("/addMoney")
    public ResponseEntity<String> addMoney(@RequestBody Account account, Authentication authentication){
        try {
            User user= (User) authentication.getPrincipal();
            int balance=walletService.addMoneyToWallet(account,user.getUsername());
            return new ResponseEntity("Updated Balance:"+balance,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/sendMoney")
    public ResponseEntity sendMoney(@RequestBody Transaction transaction, Authentication authentication){
        try {
            User user= (User) authentication.getPrincipal();
            walletService.sendMoneyToUser(transaction, user.getUsername());
            return new ResponseEntity("Transaction has been successfully completed.",HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/transactions")
    public ResponseEntity<Set<Transaction>> getTransactions(Authentication authentication){
        try{
            User user= (User) authentication.getPrincipal();
            Set<Transaction> transactionSet=walletService.getUserTransactions(user.getUsername());
            return new ResponseEntity<>(transactionSet,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
