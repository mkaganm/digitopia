package com.digitopia.orgservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "organizations")
public class OrganizationEntity {
    @Id @GeneratedValue private UUID id;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    private UUID createdBy;
    private UUID updatedBy;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(name = "normalized_name", nullable = false)
    private String normalizedName;

    @Column(nullable = false)
    private String status = "ACTIVE";

    // Getters & setters
    public UUID getId() { return id; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public UUID getCreatedBy() { return createdBy; }
    public void setCreatedBy(UUID createdBy) { this.createdBy = createdBy; }
    public UUID getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(UUID updatedBy) { this.updatedBy = updatedBy; }
    public UUID getOwnerId() { return ownerId; }
    public void setOwnerId(UUID ownerId) { this.ownerId = ownerId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getNormalizedName() { return normalizedName; }
    public void setNormalizedName(String normalizedName) { this.normalizedName = normalizedName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
