package KATA.learn.service;

import KATA.learn.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {
    private final RestTemplate restTemplate;
    private final String BASE_URL = "http://94.198.50.185:7081/api/users";
    private String sessionId;

    @Autowired
    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private void updateSession(ResponseEntity<?> response) {
        List<String> cookies = response.getHeaders().get("Set-Cookie");
        if (cookies != null && !cookies.isEmpty()) {
            sessionId = cookies.get(0);
        }
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (sessionId != null) {
            headers.set("Cookie", sessionId);
        }
        return headers;
    }

    public List<User> getAllUsers() {
        ResponseEntity<User[]> response = restTemplate.getForEntity(BASE_URL, User[].class);
        updateSession(response);
        return Arrays.asList(response.getBody());
    }

    public String addUser(User user) {
        HttpHeaders headers = getHeaders();
        HttpEntity<User> request = new HttpEntity<>(user, headers);
        ResponseEntity<String> response =
                restTemplate.exchange(BASE_URL, HttpMethod.POST, request, String.class);
        return response.getBody();
    }

    public String updateUser(User user) {
        HttpHeaders headers = getHeaders();
        HttpEntity<User> request = new HttpEntity<>(user, headers);
        ResponseEntity<String> response = restTemplate.exchange(BASE_URL, HttpMethod.PUT, request, String.class);
        return response.getBody();
    }

    public String deleteUser(Long id) {
        HttpHeaders headers = getHeaders();
        HttpEntity<User> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(BASE_URL + "/" + id, HttpMethod.DELETE, request, String.class);
        return response.getBody();
    }

    public void performActions() {
        List<User> users = getAllUsers();
        System.out.println("All Users: " + users);

        User newUser = new User(3L, "James", "Brown", (byte)101);
        String part1 = addUser(newUser);
        System.out.println("Part 1: " + part1);

        User updateUser = new User(3L, "Thomas", "Shelby", (byte)101);
        String part2 = updateUser(updateUser);
        System.out.println("Part 2: " + part2);

        String part3 = deleteUser(3L);
        System.out.println("Part 3: " + part3);

        System.out.println("Final code: " + part1 + part2 + part3);
    }
}
