package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class TransferService {
    String authToken = null;
    private final String baseUrl = "http://localhost:8080/transfer";
    private final RestTemplate restTemplate = new RestTemplate();

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

//    public makeTransfer() {
//        String url = baseUrl + "/transfer";
//        ResponseEntity<Double> response = restTemplate.exchange(url, HttpMethod.PUT, makeAuthEntity(), Double.class);
//        Double num = response.getBody();
//    }
//}
}