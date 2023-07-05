package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;


public class JdbcAccountDao implements AccountDao{
    private final JdbcTemplate jdbcTemplate;


    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public double getAccountBalance(int accountId){
        double balance = 0.0;
       String sql = "SELECT balance FROM account WHERE id = ?";

           balance = jdbcTemplate.queryForObject(sql, Double.class, accountId);

       return balance;
    }

}
