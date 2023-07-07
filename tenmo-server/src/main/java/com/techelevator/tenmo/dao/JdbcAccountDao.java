package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {
    private final JdbcTemplate jdbcTemplate;


    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccountByUserId(int userId) {
        Account account = null;
        String sql = "select account_id, user_id, balance from account where user_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            int tempAcctId = results.getInt("account_id");
            int tempUserId = results.getInt("user_id");
            double tempBalance = results.getDouble("balance");

            account = new Account(tempAcctId, tempUserId, tempBalance);

        }
        return account;

    }

    public void updateAccountBalance(int userId, int receiverId, BigDecimal amount) {
        String sql = "UPDATE  account " +
                "SET balance =CASE " +
                "WHEN user_id = ? THEN balance - ? " +
                "WHEN user_id = ? THEN balance + ? " +
                " ELSE balance " +
                " END " +
                "WHERE user_id IN (?, ?)";
        jdbcTemplate.update(sql, userId, amount, receiverId, amount, userId, receiverId);
    }
}

