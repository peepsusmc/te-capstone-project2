package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class AccountController {

    private final AccountDao accountDao;
    private final UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao){
        this.accountDao = accountDao;
        this.userDao = userDao;
    }
    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public double getAccountBalance(Principal principal){
        double accountBalance = 0.0;
        int userId = userDao.findIdByUsername(principal.getName());
        Account account = accountDao.getAccountByUserId(userId);
        accountBalance = account.getBalance();
        return accountBalance;
    }
}
