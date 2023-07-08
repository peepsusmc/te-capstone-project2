package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

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
        int userId = userDao.findIdByUsername(p.getName());
        Account account = accountDao.getAccountByUserId(userId);
        int accountFrom = account.getAccountId();
        int accountTo = transfer.getAccountTo();
        BigDecimal amount = transfer.getAmount();
        send.setTransferTypeId(2);
        send.setTransferStatusId(2);
        send.setAccountFrom(accountFrom);
        send.setAccountTo(accountTo);
        send.setAmount(amount);
        transferDao.createTransfer(send);
        accountDao.updateAccountBalance(userId, accountTo, amount);
    }

    @RequestMapping(value = "/mytransfers", method = RequestMethod.GET)
    public List<TransferDto> getTransferByAccountId(Principal p){
        int userId = userDao.findIdByUsername(p.getName());
        Account a = accountDao.getAccountByUserId(userId);
        int accountId = a.getAccountId();
        return transferDao.getTransferByAccountId(accountId);

    }
}
