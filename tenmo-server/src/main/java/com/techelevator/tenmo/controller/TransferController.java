package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
public class TransferController {

    private final UserDao userDao;
    private final TransferDao transferDao;
    private final AccountDao accountDao;

    public TransferController(AccountDao accountDao, UserDao userDao, TransferDao transferDao){
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transferDao = transferDao;
    }
    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public void createTransfer(Principal p, @RequestBody Transfer transfer) {
        Transfer send = new Transfer();
        int userIdFrom = userDao.findIdByUsername(p.getName());
        Account sender = accountDao.getAccountByUserId(userIdFrom);
        int accountIdFrom = sender.getAccountId();
        Account receiver = accountDao.getAccountByUserId(transfer.getAccountTo());
        int accountIdTo = receiver.getAccountId();
        int userIdTo = transfer.getAccountTo();
        BigDecimal amount = transfer.getAmount();
        send.setTransferTypeId(2);
        send.setTransferStatusId(2);
        send.setAccountFrom(accountIdFrom);
        send.setAccountTo(accountIdTo);
        send.setAmount(amount);
        transferDao.createTransfer(send);
        accountDao.updateAccountBalance(userId, accountTo, amount);
    }
}
