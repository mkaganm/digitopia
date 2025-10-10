package com.digitopia.userservice.service;

import com.digitopia.userservice.domain.UserEntity;
import com.digitopia.userservice.exception.ResourceNotFoundException;
import com.digitopia.userservice.repo.UserRepository;
import com.digitopia.userservice.user.api.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.Locale;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) { this.repo = repo; }

    @Transactional
    public UserEntity create(UserEntity u) {
        // ensure unique email at app-layer (still keep DB unique index)
        repo.findByEmailIgnoreCase(u.getEmail()).ifPresent(x -> {
            throw new IllegalArgumentException("Email already exists");
        });

        // derive normalizedName safely (ASCII-only, lowercase, alphanumeric)
        u.setNormalizedName(normalizeAscii(u.getFullName()));

        // default status if not provided
        if (u.getStatus() == null) {
            u.setStatus("PENDING"); // if you later switch to enum, adjust mapping
        }

        return repo.save(u);
    }

    @Transactional(readOnly = true)
    public Page<UserEntity> searchByName(String q, Pageable p) {
        return repo.findByNormalizedNameContainingIgnoreCase(q, p);
    }

    // Map entity to DTO (keep public surface small)
    @Transactional(readOnly = true)
    public UserDto getById(UUID id) {
        UserEntity u = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        // status is String in your entity; do not call .name()
        return new UserDto(u.getId(), u.getEmail(), u.getStatus(), u.getFullName());
    }

    // --- helpers ---

    /**
     * Normalize to lowercase ASCII and keep only [a-z0-9].
     * Example: "Çağrı Öztürk!" -> "cagriozturk"
     */
    private static String normalizeAscii(String input) {
        if (input == null) return "";
        String lower = input.toLowerCase(Locale.ENGLISH);
        String decomposed = Normalizer.normalize(lower, Normalizer.Form.NFD);
        String stripped = decomposed.replaceAll("\\p{M}+", ""); // remove diacritics
        return stripped.replaceAll("[^a-z0-9]", "");            // keep alphanumeric only
    }
}
