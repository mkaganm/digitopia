package com.digitopia.userservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity @Table(name = "users")
public class UserEntity {
    @Id @GeneratedValue private UUID id;

    @CreationTimestamp @Column(name="created_at", updatable=false)
    private OffsetDateTime createdAt;
    @UpdateTimestamp @Column(name="updated_at")
    private OffsetDateTime updatedAt;

    private UUID createdBy; private UUID updatedBy;

    @Email @NotBlank @Column(nullable=false, updatable=false)
    private String email;

    @NotBlank
    @Pattern(regexp="^[\\p{L} ]+$")
    @Column(name="full_name", nullable=false)
    private String fullName;

    @Column(name="normalized_name", nullable=false)
    private String normalizedName;

    @NotBlank @Column(nullable=false)
    private String status = "PENDING";

    // getters/setters
    public UUID getId() { return id; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public UUID getCreatedBy() { return createdBy; }
    public void setCreatedBy(UUID createdBy) { this.createdBy = createdBy; }
    public UUID getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(UUID updatedBy) { this.updatedBy = updatedBy; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getNormalizedName() { return normalizedName; }
    public void setNormalizedName(String normalizedName) { this.normalizedName = normalizedName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
