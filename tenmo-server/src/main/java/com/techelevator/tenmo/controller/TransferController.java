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
        int senderId = userDao.findIdByUsername(p.getName());
        Account sender = accountDao.getAccountByUserId(senderId);
        Account receiver = accountDao.getAccountByUserId(transfer.getAccountTo());
        int receiverId = receiver.getUserId();
        int accountFromId = sender.getAccountId();
        int accountToId = receiver.getAccountId();
        BigDecimal amount = transfer.getAmount();
        Transfer send = new Transfer();
        send.setTransferTypeId(2);
        send.setTransferStatusId(2);
        send.setAccountFrom(accountFromId);
        send.setAccountTo(accountToId);
        send.setAmount(amount);
        transferDao.createTransfer(send);
        accountDao.updateAccountBalance(senderId, receiverId, amount);
    }

    @RequestMapping(value = "/request", method = RequestMethod.POST)
    public void createTransferRequest(Principal p, @RequestBody Transfer request) {
        int requesterUserId = userDao.findIdByUsername(p.getName());
        Account requester = accountDao.getAccountByUserId(requesterUserId);
        Account requested = accountDao.getAccountByUserId(request.getAccountFrom());
        int requestedUserId = requested.getUserId();
        int requesterAccountId = requester.getAccountId();
        int requestedAccountId = requested.getAccountId();
        BigDecimal amount = request.getAmount();
        Transfer send = new Transfer();
        send.setTransferTypeId(1);
        send.setTransferStatusId(1);
        send.setAccountFrom(requestedAccountId);
        send.setAccountTo(requesterAccountId);
        send.setAmount(amount);
        transferDao.createTransfer(send);
    }
}
