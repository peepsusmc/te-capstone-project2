package com.techelevator.tenmo.services;

import org.springframework.web.client.RestTemplate;

public class TransferService {

    private final String baseUrl = "http://localhost:8080/transfer" ;
    private final RestTemplate restTemplate =  new RestTemplate();


}
