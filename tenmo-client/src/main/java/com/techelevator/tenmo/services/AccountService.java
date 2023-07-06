package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AccountService {
    String authToken = null;
    private final String baseUrl = "http://localhost:8080/" ;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
    private final RestTemplate restTemplate =  new RestTemplate();

    public double getBalance(){
        double balance = 0.0;
        String url = baseUrl + "account/balance";
        ResponseEntity<Double> response = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(),Double.class);
        Double num = response.getBody();
        balance = num.doubleValue();
        return balance;


    }
}
