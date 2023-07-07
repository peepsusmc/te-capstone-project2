package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.security.Principal;

public class TransferService {

    String authToken = null;
    private final String baseUrl = "http://localhost:8080/transfer";
    private final RestTemplate restTemplate = new RestTemplate();

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

//    HttpEntity<Void> makeAuthEntity() {
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(authToken);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<Transfer> requestEntity = new HttpEntity<>(transfer, headers);
//
//    public void makeTransfer(int sender, int receiver, Bigdecimanl amount) {
//        String url = baseUrl + "/transfer";
//        ResponseEntity<Double> response = restTemplate.exchange(url, HttpMethod.PUT, makeAuthEntity(), Double.class);
//        Double num = response.getBody();
//    }
//}
}