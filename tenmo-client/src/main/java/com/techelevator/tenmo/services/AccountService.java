package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AccountService {
    private final String baseUrl = "http://localhost:8080/" ;
    private final RestTemplate restTemplate =  new RestTemplate();

    String token = null;

    public void setToken(String token) {
        this.token = token;
    }

    public HttpEntity<Void> authEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }
    public double getAccountBalance(){
        String url = baseUrl + "/balance";
        ResponseEntity<Double> response = restTemplate.exchange(url, HttpMethod.GET, authEntity(),Double.class);

        Double tmp = response.getBody();
        return tmp.doubleValue();
    }
}
