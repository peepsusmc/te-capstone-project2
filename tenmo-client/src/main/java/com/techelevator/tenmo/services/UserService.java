package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    private final String baseUrl = "http://localhost:8080/";

    private final RestTemplate restTemplate = new RestTemplate();

    public List<User> getUsers(){
        ResponseEntity<List<User>> entity = restTemplate.exchange(baseUrl + "/users", HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });
        if (entity.getStatusCode() == HttpStatus.OK){
            List<User> users = entity.getBody();
            if (users !=null){
                return users;
            }
        }else{
            System.out.println("Error "+ entity.getStatusCodeValue());
        }
        return new ArrayList<>();

    }

    public void displayUsers(){
        List<User> users = getUsers();
        for (User user : users){
            int userId = user.getId();
            String username = user.getUsername();

            System.out.println("User ID: "+userId);
            System.out.println("Username: "+username);
        }
    }
}
