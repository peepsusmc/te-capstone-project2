package com.techelevator.tenmo.services;

import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import com.techelevator.tenmo.model.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    String authToken = null;
    private final String baseUrl = "http://localhost:8080/";

    private final RestTemplate restTemplate = new RestTemplate();

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    public List<User> getUsers() {
        try {
            ResponseEntity<List<User>> entity = restTemplate.exchange(baseUrl + "/users", HttpMethod.GET, makeAuthEntity(), new ParameterizedTypeReference<>() {
            });
            if (entity.getStatusCode() == HttpStatus.OK) {
                List<User> users = entity.getBody();
                if (users != null) {
                    return users;
                }
            } else {
                System.out.println("Error " + entity.getStatusCodeValue());
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return new ArrayList<>();

    }

    public void displayUsers() {
        List<User> users = getUsers();
        System.out.printf("%-10s %-20s %n", "User Id", "User Name");
        for (User user : users) {
            int userId = user.getId();
            String username = user.getUsername();
            System.out.printf("%-10d %-20s%n", userId, username);

        }
    }
}
