package com.digitopia.orgservice.client;

import com.digitopia.orgservice.client.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

/**
 * Simple HTTP client for user-service using RestTemplate.
 * No WebFlux/Netty dependencies required.
 */
@Component
@RequiredArgsConstructor
public class UserClient {

    private final RestTemplate restTemplate;

    @Value("${remote.user-service.base-url}")
    private String userBase; // e.g. http://user-service:8081

    public UserDto getById(UUID id) {
        try {
            return restTemplate.getForObject(userBase + "/api/v1/users/{id}", UserDto.class, id);
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("User not found: " + id);
        } catch (RestClientException e) {
            throw new IllegalStateException("User service unavailable", e);
        }
    }
}
