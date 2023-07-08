package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

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

    public void requestTransfer(int receiver, BigDecimal amount) {
        String url = baseUrl + "request";
        Transfer request = new Transfer();
        request.setAccountFrom(receiver);
        request.setAmount(amount);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Transfer> entity = new HttpEntity<>(request, headers);

        try {
            restTemplate.postForObject(url, entity, Transfer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

    }


    public List<TransferDto> getTransfersByUser() {
        String url = baseUrl + "/mytransfers";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<List<TransferDto>> response = restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<TransferDto>>() {
            });
            List<TransferDto> transferDtos = response.getBody();
            return transferDtos;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            return Collections.emptyList();
        }
    }

    public void displayTransfers() {
        List<TransferDto> transferDtos = getTransfersByUser();
        System.out.printf("%-10s %-20s %-30s%n", "Transfer Id", "FROM/TO", "Amount");
        for (TransferDto transferDto : transferDtos) {
            int transferId = transferDto.getTransferId();
            String from = transferDto.getSenderName();
            String to = transferDto.getReceiverName();
            BigDecimal amount = transferDto.getAmount();
            System.out.printf("%-10d %-20s %-30s%n", transferId, from + "/" + to, amount);

        }
    }

    public void displayTransferDetails(int transferId) {
        List<TransferDto> transferDtos = getTransfersByUser();
        boolean gotIt = false;
        for (TransferDto transferDto : transferDtos) {
            if (transferDto.getTransferId() == transferId) {
                System.out.println("Transfer Details ");
                System.out.println("Transfer ID: " + transferId);
                System.out.println("From: " + transferDto.getSenderName());
                System.out.println("To: " + transferDto.getReceiverName());
                System.out.println("Type: " + transferDto.getTransferType());
                System.out.println("Status: " + transferDto.getTransferStatus());
                System.out.println("Amount: " + transferDto.getAmount());
                gotIt = true;
                break;
            }
        }
        if (!gotIt) {
            System.out.println("Transfer with ID " + transferId + "does not exist.");
        }
    }
}