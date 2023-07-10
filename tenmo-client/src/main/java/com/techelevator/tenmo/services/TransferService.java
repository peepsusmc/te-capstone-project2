package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PathVariable;
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
    public static String baseUrl = "http://localhost:8080/";

    private final RestTemplate restTemplate = new RestTemplate();

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Transfer makeTransfer(int receiver, BigDecimal amount) {
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
            System.out.println("Transfer successful!");
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            System.out.println("Error occurred: " + e.getMessage());
        }
        return transfer;
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

    public boolean updateTransfer(@PathVariable("id") int transferId, int status) {
        boolean success = false;
        String url = baseUrl + "transfer/" + transferId;
        Transfer transfer = new Transfer();
        transfer.setTransferId(transferId);
        transfer.setTransferStatusId(status);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        try {
            restTemplate.put(url, entity);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
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

    public List<TransferDto> getRequestsByUser() {
        String url = baseUrl + "/myrequests";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<List<TransferDto>> response = restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<TransferDto>>() {
        });
        List<TransferDto> transferDtos = response.getBody();
        return transferDtos;
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

    public void displayRequests() {
        List<TransferDto> transferDtos = getRequestsByUser();
        System.out.printf("%-10s %-20s %-30s%n", "Transfer Id", "FROM", "Amount");
        for (TransferDto transferDto : transferDtos) {
            int transferId = transferDto.getTransferId();
            String from = transferDto.getSenderName();
            String to = transferDto.getReceiverName();
            BigDecimal amount = transferDto.getAmount();
            System.out.printf("%-10d %-20s %-30s%n", transferId, to, amount);

        }
    }
}