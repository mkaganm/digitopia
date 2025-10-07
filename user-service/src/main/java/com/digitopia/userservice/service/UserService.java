package com.digitopia.userservice.service;

import com.digitopia.userservice.domain.UserEntity;
import com.digitopia.userservice.repo.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository repo;
    public UserService(UserRepository repo){ this.repo = repo; }

    @Transactional
    public UserEntity create(UserEntity u){
        repo.findByEmailIgnoreCase(u.getEmail()).ifPresent(x -> {
            throw new IllegalArgumentException("Email already exists");
        });
        u.setNormalizedName(u.getFullName().toLowerCase().replaceAll("[^a-z0-9]", ""));
        if (u.getStatus() == null) u.setStatus("PENDING");
        return repo.save(u);
    }

    public Page<UserEntity> searchByName(String q, Pageable p){
        return repo.findByNormalizedNameContainingIgnoreCase(q, p);
    }
}
