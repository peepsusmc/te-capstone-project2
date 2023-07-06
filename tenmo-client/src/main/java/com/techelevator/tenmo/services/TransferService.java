package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.security.Principal;

public class TransferService {

    String authToken = null;
    private final String baseUrl = "http://localhost:8080" ;
    private final RestTemplate restTemplate =  new RestTemplate();
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String makeTransfer(int receiver, BigDecimal amount) {
        String url = baseUrl + "/transfer";

        Transfer transfer = new Transfer();
        transfer.setAccountTo(receiver);
        transfer.setAmount(amount);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Transfer> requestEntity = new HttpEntity<>(transfer, headers);

        ResponseEntity<Transfer> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Transfer.class);

        return "Transfer complete";
    }
}