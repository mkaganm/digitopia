package com.digitopia.orgservice.service;

import com.digitopia.orgservice.client.UserClient;
import com.digitopia.orgservice.domain.OrganizationEntity;
import com.digitopia.orgservice.exception.ResourceNotFoundException; // 404
import com.digitopia.orgservice.repo.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository repo;
    private final UserClient userClient;

    @Transactional
    public OrganizationEntity create(OrganizationEntity o) {
        if (o.getOwnerId() == null) throw new IllegalArgumentException("ownerId must be provided");
        if (o.getName() == null || o.getName().isBlank()) throw new IllegalArgumentException("name must be provided");

        // 400: name must be unique
        repo.findByNameIgnoreCase(o.getName())
                .ifPresent(x -> { throw new IllegalArgumentException("Organization name already exists"); });

        // normalize + validate owner exists (404 -> ResourceNotFoundException inside client or here)
        o.setNormalizedName(normalizeAscii(o.getName()));
        userClient.getById(o.getOwnerId()); // throws if not found

        return repo.save(o);
    }

    @Transactional
    public OrganizationEntity update(OrganizationEntity o) {
        if (o.getId() == null) throw new IllegalArgumentException("id must be provided");
        if (o.getOwnerId() == null) throw new IllegalArgumentException("ownerId must be provided");

        // 404: entity to update must exist
        OrganizationEntity current = repo.findById(o.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found: " + o.getId()));

        // if name provided, re-normalize and (optionally) check uniqueness
        if (o.getName() != null && !o.getName().isBlank()) {
            repo.findByNameIgnoreCase(o.getName())
                    .filter(e -> !e.getId().equals(o.getId()))
                    .ifPresent(x -> { throw new IllegalArgumentException("Organization name already exists"); });
            current.setName(o.getName());
            current.setNormalizedName(normalizeAscii(o.getName()));
        }

        // validate owner (exists in user-service)
        userClient.getById(o.getOwnerId());
        current.setOwnerId(o.getOwnerId());

        // other updatable fields here (if any)

        return repo.save(current);
    }

    @Transactional(readOnly = true)
    public Page<OrganizationEntity> search(String q, Pageable p) {
        return repo.findByNormalizedNameContainingIgnoreCase(q, p);
    }

    @Transactional(readOnly = true)
    public OrganizationEntity get(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found: " + id));
    }

    private static String normalizeAscii(String input) {
        if (input == null) return "";
        // 1) Turkish-aware lowercase
        String lower = input.toLowerCase(java.util.Locale.forLanguageTag("tr"));

        // 2) Map Turkish-specific letters to ASCII equivalents
        lower = lower
                .replace('ı', 'i')
                .replace('İ', 'i') // in case upper slipped in
                .replace('ğ', 'g').replace('Ğ', 'g')
                .replace('ş', 's').replace('Ş', 's')
                .replace('ö', 'o').replace('Ö', 'o')
                .replace('ü', 'u').replace('Ü', 'u')
                .replace('ç', 'c').replace('Ç', 'c');

        // 3) Remove any remaining diacritics (safety net)
        String decomposed = java.text.Normalizer.normalize(lower, java.text.Normalizer.Form.NFD);
        String stripped = decomposed.replaceAll("\\p{M}+", "");

        // 4) Keep only [a-z0-9]
        return stripped.replaceAll("[^a-z0-9]", "");
    }
}
