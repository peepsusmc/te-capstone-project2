package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountDao accountDao;

    public AccountController(AccountDao accountDao){
        this.accountDao = accountDao;
    }
    @RequestMapping("{accountId}/balance")
    public double getAccountBalance(@PathVariable int accountId){
        double accountBalance = accountDao.getAccountBalance(accountId);

        return accountBalance;
    }
}
