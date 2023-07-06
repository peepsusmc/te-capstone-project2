package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/transfer")
public class TransferController {
    private final AccountDao accountDao;
    private final UserDao userDao;

    public TransferController(AccountDao accountDao, UserDao userDao){
        this.accountDao = accountDao;
        this.userDao = userDao;
    }
    @RequestMapping(value = "/transfer", method = RequestMethod.PUT)
    public double makeTransfer(Principal p, int receiverId, double amount){
        int userId = userDao.findIdByUsername(p.getName());
        Account senderAccount = accountDao.getAccountByUserId(userId);
        Account receivingAccount = accountDao.getAccountByUserId(receiverId);
        double newSenderBalance = senderAccount.getBalance() - amount;
        double newReceivingBalance = receivingAccount.getBalance() + amount;
        senderAccount.setBalance(newSenderBalance);
        receivingAccount.setBalance(newReceivingBalance);

        return newSenderBalance;
    }

}
