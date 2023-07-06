package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{
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
            double tempAmount = results.getDouble("amount");

            Transfer transfer = new Transfer(tempTransId, tempTypeId, tempStatusId, tempActFromId, tempActToId, tempAmount);
            transfers.add(transfer);
        }
        return transfers;
    }
}
