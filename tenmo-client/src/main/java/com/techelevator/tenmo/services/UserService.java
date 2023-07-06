package com.techelevator.tenmo.services;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import com.techelevator.tenmo.model.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
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
        return new ArrayList<>();

    }

    public void displayUsers() {
        List<User> users = getUsers();
        for (User user : users) {
            int userId = user.getId();
            String username = user.getUsername();

            System.out.println("User ID: " + userId);
            System.out.println("Username: " + username);
        }
    }
}
