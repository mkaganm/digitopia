package com.digitopia.orgservice.repo;

import com.digitopia.orgservice.domain.OrganizationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<OrganizationEntity, UUID> {
    Optional<OrganizationEntity> findByNameIgnoreCase(String name);
    Page<OrganizationEntity> findByNormalizedNameContainingIgnoreCase(String name, Pageable pageable);
}
