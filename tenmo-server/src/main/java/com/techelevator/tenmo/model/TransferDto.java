package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferDto {
    private int userId;
    private int receiverId;
    private BigDecimal amount;

    public TransferDto() {
    }

    public int getUserId() {
        return userId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
