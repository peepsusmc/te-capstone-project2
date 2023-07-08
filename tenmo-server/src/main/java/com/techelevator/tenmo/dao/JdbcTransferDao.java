package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> showAllTransfers(int accountId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer where account_from = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        while (results.next()) {
            int tempTransId = results.getInt("transfer_id");
            int tempTypeId = results.getInt("transfer_type_id");
            int tempStatusId = results.getInt("transfer_status_id");
            int tempActFromId = results.getInt("account_from");
            int tempActToId = results.getInt("account_to");
            BigDecimal tempAmount = results.getBigDecimal("amount");

            Transfer transfer = new Transfer(tempTransId, tempTypeId, tempStatusId, tempActFromId, tempActToId, tempAmount);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public void createTransfer(Transfer transfer) {
        String sqlSender = "SELECT account_id FROM account WHERE account_id = ?";
        String sqlReceiver = "SELECT account_id FROM account WHERE account_id = ?";
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, (" + sqlSender + "), (" + sqlReceiver + "), CAST(? AS numeric))";
        jdbcTemplate.update(
                sql,
                transfer.getTransferTypeId(),
                transfer.getTransferStatusId(),
                transfer.getAccountFrom(),
                transfer.getAccountTo(),
                transfer.getAmount().toPlainString()
        );
    }

    @Override
    public void updateTransfer(Transfer transfer) {
        String sql = "UPDATE transfer SET transfer_status_id = ?" +
                "WHERE transfer_id = ?;";
        int transId = transfer.getTransferId();
        int statusId = transfer.getTransferStatusId();

        jdbcTemplate.update(sql, statusId, transId);
    }

    @Override
    public List<TransferDto> getTransferByAccountId(int accountId) {
        List<TransferDto> transfers = new ArrayList<>();
        String sql = "SELECT t.transfer_id, t.amount, u.username AS sender_name, u2.username AS receiver_name, " +
                "tt.transfer_type_desc, ts.transfer_status_desc " +
                "FROM transfer t " +
                "JOIN account a1 ON t.account_from = a1.account_id " +
                "JOIN account a2 ON t.account_to = a2.account_id " +
                "JOIN tenmo_user u ON a1.user_id = u.user_id " +
                "JOIN tenmo_user u2 ON a2.user_id = u2.user_id " +
                "JOIN transfer_type tt ON t.transfer_type_id = tt.transfer_type_id " +
                "JOIN transfer_status ts ON t.transfer_status_id = ts.transfer_status_id " +
                "WHERE a1.account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        while (results.next()) {
            TransferDto transferDto = new TransferDto();
            transferDto.setTransferId(results.getInt("transfer_id"));
            transferDto.setAmount(results.getBigDecimal("amount"));
            transferDto.setSenderName(results.getString("sender_name"));
            transferDto.setReceiverName(results.getString("receiver_name"));
            transferDto.setTransferType(results.getString("transfer_type_desc"));
            transferDto.setTransferStatus(results.getString("transfer_status_desc"));
            transfers.add(transferDto);
        }
        return transfers;


    }

    @Override
    public List<TransferDto> getRequestsByAccountId(int accountId) {
        List<TransferDto> transfers = new ArrayList<>();
        String sql = "SELECT t.transfer_id, t.amount, u.username AS sender_name, u2.username AS receiver_name, " +
                "tt.transfer_type_desc, ts.transfer_status_desc " +
                "FROM transfer t " +
                "JOIN account a1 ON t.account_from = a1.account_id " +
                "JOIN account a2 ON t.account_to = a2.account_id " +
                "JOIN tenmo_user u ON a1.user_id = u.user_id " +
                "JOIN tenmo_user u2 ON a2.user_id = u2.user_id " +
                "JOIN transfer_type tt ON t.transfer_type_id = tt.transfer_type_id " +
                "JOIN transfer_status ts ON t.transfer_status_id = ts.transfer_status_id " +
                "WHERE a1.account_id = ? AND t.transfer_status_id = 1;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        while (results.next()) {
            TransferDto transferDto = new TransferDto();
            transferDto.setTransferId(results.getInt("transfer_id"));
            transferDto.setAmount(results.getBigDecimal("amount"));
            transferDto.setSenderName(results.getString("sender_name"));
            transferDto.setReceiverName(results.getString("receiver_name"));
            transferDto.setTransferType(results.getString("transfer_type_desc"));
            transferDto.setTransferStatus(results.getString("transfer_status_desc"));
            transfers.add(transferDto);
        }
        return transfers;
    }

    @Override
    public Transfer getTransferById(int transferId) {
        Transfer transfer = new Transfer();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer where transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            transfer.setTransferId(results.getInt("transfer_id"));
            transfer.setTransferTypeId(results.getInt("transfer_type_id"));
            transfer.setTransferStatusId(results.getInt("transfer_status_id"));
            transfer.setAccountFrom(results.getInt("account_from"));
            transfer.setAccountTo(results.getInt("account_to"));
            transfer.setAmount(results.getBigDecimal("amount"));
        }
        return transfer;
    }

}




