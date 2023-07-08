package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.model.User;

import java.util.List;

public interface TransferDao {
    List<Transfer> showAllTransfers(int accountId);
    void createTransfer(Transfer transfer);
    List<TransferDto>getTransferByAccountId(int accountId);

}
