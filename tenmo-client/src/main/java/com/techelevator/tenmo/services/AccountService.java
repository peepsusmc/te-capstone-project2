package com.techelevator.tenmo.services;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AccountService {
    private final String baseUrl = "http://localhost:8080/" ;
    private final RestTemplate restTemplate =  new RestTemplate();

    public double getAccountBalance(int accountId){
        String url = baseUrl + "account/" + accountId + "/balance";
        ResponseEntity<Double> response = restTemplate.exchange(url, HttpMethod.GET, null,Double.class);

        return response.getBody();


    }
}
