package com.techelevator.tenmo.model;

public class Transfer {

    private int transferId;
    private int transferTypeId;
    private int transferStatusId;
    private int accountFrom;
    private int accountTo;
    private double amount;

    public Transfer(){};
    public Transfer(int transferId, int transferTypeId, int transferStatusId, int accountFrom, int accountTo, double amount) {
        this.transferId = transferId;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public int getTransferId() {
        return transferId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public int getAccountFrom() {
        return accountFrom;
    }
    public int getAccountTo() {
        return accountTo;
    }

    public double getAmount() {
        return amount;
    }
}
