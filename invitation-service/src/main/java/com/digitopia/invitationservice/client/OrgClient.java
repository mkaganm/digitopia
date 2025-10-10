package com.digitopia.invitationservice.client;

import com.digitopia.invitationservice.client.dto.OrganizationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrgClient {
    private final RestTemplate rest;
    @Value("${remote.org-service.base-url}")
    private String orgBase; // http://org-service:8082

    public OrganizationDto getById(UUID id) {
        try {
            return rest.getForObject(orgBase + "/api/v1/orgs/{id}", OrganizationDto.class, id);
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("Organization not found: " + id);
        } catch (RestClientException e) {
            throw new IllegalStateException("Org service unavailable", e);
        }
    }
}
