package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;



public class TransferService {

    String authToken = null;
    private final String baseUrl = "http://localhost:8080/";

    private final RestTemplate restTemplate = new RestTemplate();

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void makeTransfer(int receiver, BigDecimal amount) {
        String url = baseUrl + "transfer";
        Transfer transfer = new Transfer();
        transfer.setAccountTo(receiver);
        transfer.setAmount(amount);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        try {
            restTemplate.postForObject(url, entity, Transfer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
    }
    public void updateBalance(int receiver, BigDecimal amount) {

    }
}