package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private final UserDao userDao;
    private final TransferDao transferDao;
    private final AccountDao accountDao;

    public TransferController(AccountDao accountDao, UserDao userDao, TransferDao transferDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transferDao = transferDao;
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public ResponseEntity<?> createTransfer(Principal p, @RequestBody Transfer transfer) {
        int senderId = userDao.findIdByUsername(p.getName());
        Account sender = accountDao.getAccountByUserId(senderId);
        Account receiver = accountDao.getAccountByUserId(transfer.getAccountTo());
        int receiverId = receiver.getUserId();
        int accountFromId = sender.getAccountId();
        int accountToId = receiver.getAccountId();
        BigDecimal amount = transfer.getAmount();
        if (accountFromId == accountToId) {
            String error = "Cannot send money to yourself!";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        Transfer send = new Transfer();
        send.setTransferTypeId(2);
        send.setTransferStatusId(2);
        send.setAccountFrom(accountFromId);
        send.setAccountTo(accountToId);
        send.setAmount(amount);
        transferDao.createTransfer(send);
        accountDao.updateAccountBalance(senderId, receiverId, amount);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "transfer/{id}", method = RequestMethod.PUT)
    public void updateTransfer(Principal p, @RequestBody Transfer transfer) {
        Transfer updatedTransfer = transferDao.getTransferById(transfer.getTransferId());
        int senderId = userDao.findIdByUsername(p.getName());
        int receiverId = userDao.findUserIdByAccount(updatedTransfer.getAccountTo());
        BigDecimal amount = updatedTransfer.getAmount();
        updatedTransfer.setTransferStatusId(transfer.getTransferStatusId());
        try {
            transferDao.updateTransfer(updatedTransfer);
            if (transfer.getTransferStatusId() == 2) {
                accountDao.updateAccountBalance(senderId, receiverId, amount);
            }
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found.");
        }
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

    @RequestMapping(value = "/mytransfers", method = RequestMethod.GET)
    public List<TransferDto> getTransferByAccountId(Principal p) {
        int userId = userDao.findIdByUsername(p.getName());
        Account a = accountDao.getAccountByUserId(userId);
        int accountId = a.getAccountId();
        return transferDao.getTransferByAccountId(accountId);
    }

    @RequestMapping(value = "/myrequests", method = RequestMethod.GET)
    public List<TransferDto> getRequestsByAccountId(Principal p) {
        int userId = userDao.findIdByUsername(p.getName());
        Account a = accountDao.getAccountByUserId(userId);
        int accountId = a.getAccountId();
        return transferDao.getRequestsByAccountId(accountId);
    }
}
