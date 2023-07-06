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
@RequestMapping("/transfer")
public class TransferController {
    private final AccountDao accountDao;
    private final UserDao userDao;
    private final TransferDao transferDao;

    public TransferController(AccountDao accountDao, UserDao userDao, TransferDao transferDao){
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transferDao = transferDao;
    }
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Transfer createTransfer(Principal p, @RequestBody Transfer transfer) {
        Transfer send = new Transfer();
        int userId = userDao.findIdByUsername(p.getName());
        send.setTransferTypeId(2);
        send.setTransferStatusId(2);
        send.setAccountFrom(userId);
        send.setAccountTo(transfer.getAccountTo());
        send.setAmount(transfer.getAmount());
        return send;
    }
}
