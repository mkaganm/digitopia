package com.digitopia.orgservice.service;

import com.digitopia.orgservice.domain.OrganizationEntity;
import com.digitopia.orgservice.repo.OrganizationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrganizationService {
    private final OrganizationRepository repo;
    public OrganizationService(OrganizationRepository repo){ this.repo = repo; }

    @Transactional
    public OrganizationEntity create(OrganizationEntity o){
        repo.findByNameIgnoreCase(o.getName()).ifPresent(x -> {
            throw new IllegalArgumentException("Organization name already exists");
        });
        o.setNormalizedName(o.getName().toLowerCase().replaceAll("[^a-z0-9]", ""));
        return repo.save(o);
    }

    public Page<OrganizationEntity> search(String q, Pageable p){
        return repo.findByNormalizedNameContainingIgnoreCase(q, p);
    }

    public java.util.Optional<OrganizationEntity> get(java.util.UUID id){
        return repo.findById(id);
    }
}
