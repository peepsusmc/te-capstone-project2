package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.ArrayList;
import java.util.List;


public class JdbcAccountDao implements AccountDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public double getAccountBalance() {
        double balance = 0;
        String sql =
    }

//    @Override
//    public List<Account> getAccounts() {
//        List<Account> accounts = new ArrayList<>();
//        String sql = "SELECT account_id, user_id, balance FROM account ORDER BY account_id";
//        try {
//            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
//            while (results.next()) {
//                accounts.add(mapRowToAccount(results));
//            }
//        } catch (CannotGetJdbcConnectionException e) {
//            throw new DaoException("Unable to connect to server or database", e);
//        }
//        return accounts;
//    }

    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setAccountId(rowSet.getInt("account_id"));
        account.setUserId(rowSet.getInt("user_id"));
        account.setBalance(rowSet.getDouble("balance"));
        return account;
    }
}
