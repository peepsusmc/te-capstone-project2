package com.techelevator.tenmo.services;


import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;


public class AccountService {
    String authToken = null;
    private final String baseUrl = "http://localhost:8080/";

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }

    private final RestTemplate restTemplate = new RestTemplate();

    public double getBalance() {
        double balance = 0.0;
        String url = baseUrl + "/balance";
        try {
            ResponseEntity<Double> response = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(), Double.class);
            Double num = response.getBody();
            balance = num.doubleValue();
        } catch(RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }

}

