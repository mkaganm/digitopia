package com.digitopia.orgservice.service;

import com.digitopia.orgservice.client.UserClient;                 // <-- REST call to user-service
import com.digitopia.orgservice.domain.OrganizationEntity;         // <-- your entity type
import com.digitopia.orgservice.repo.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository repo;
    private final UserClient userClient; // <-- injected HTTP client

    @Transactional
    public OrganizationEntity create(OrganizationEntity o) {
        // Ensure unique organization name at app-layer (still keep DB unique index)
        repo.findByNameIgnoreCase(o.getName()).ifPresent(x -> {
            throw new IllegalArgumentException("Organization name already exists");
        });

        // Derive normalizedName safely (ASCII-only, lowercase, alphanumeric)
        o.setNormalizedName(normalizeAscii(o.getName()));

        // Validate ownerId exists in user-service
        if (o.getOwnerId() == null) {
            throw new IllegalArgumentException("ownerId must be provided");
        }
        userClient.getById(o.getOwnerId()); // throws if not found

        return repo.save(o);
    }

    @Transactional
    public OrganizationEntity update(OrganizationEntity o) {
        // Optional: re-normalize when name changes
        if (o.getName() != null) {
            o.setNormalizedName(normalizeAscii(o.getName()));
        }

        // Re-validate owner if provided/changed
        if (o.getOwnerId() == null) {
            throw new IllegalArgumentException("ownerId must be provided");
        }
        userClient.getById(o.getOwnerId());

        return repo.save(o);
    }

    @Transactional(readOnly = true)
    public Page<OrganizationEntity> search(String q, Pageable p) {
        return repo.findByNormalizedNameContainingIgnoreCase(q, p);
    }

    @Transactional(readOnly = true)
    public Optional<OrganizationEntity> get(UUID id) {
        return repo.findById(id);
    }

    // --- helpers ---

    /**
     * Normalize to lowercase ASCII and keep only [a-z0-9].
     * Example: "Çağrı Öztürk A.Ş." -> "cagriozturkas"
     */
    private static String normalizeAscii(String input) {
        if (input == null) return "";
        String lower = input.toLowerCase(Locale.ENGLISH);
        String decomposed = Normalizer.normalize(lower, Normalizer.Form.NFD);
        String stripped = decomposed.replaceAll("\\p{M}+", ""); // remove diacritics
        return stripped.replaceAll("[^a-z0-9]", "");            // keep alphanumeric only
    }
}
