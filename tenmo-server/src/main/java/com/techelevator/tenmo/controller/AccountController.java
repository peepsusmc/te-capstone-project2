package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
public class AccountController {

    private final AccountDao accountDao;
    private final UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao){
        this.accountDao = accountDao;
        this.userDao = userDao;
    }
    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    public double getBalance(Principal p){
        double balance = 0.0;
        int userId = userDao.findIdByUsername(p.getName());
        Account account = accountDao.getAccountByUserId(userId);
        balance = account.getBalance();

        return balance;
    }

}
