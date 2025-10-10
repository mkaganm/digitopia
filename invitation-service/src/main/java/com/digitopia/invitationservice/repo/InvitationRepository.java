package com.digitopia.invitationservice.repo;

import com.digitopia.invitationservice.domain.InvitationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvitationRepository extends JpaRepository<InvitationEntity, UUID> {

    // Fetch a specific invitation by (org, user, status)
    Optional<InvitationEntity> findByOrganizationIdAndInvitedUserIdAndStatus(
            UUID orgId, UUID userId, String status
    );

    // Fast existence check for "only one PENDING per (org, user)"
    boolean existsByOrganizationIdAndInvitedUserIdAndStatus(
            UUID orgId, UUID userId, String status
    );

    // Lookups
    List<InvitationEntity> findByOrganizationId(UUID orgId);
    List<InvitationEntity> findByInvitedUserId(UUID userId);

    // --- Optional: for daily expiry job (createdAt + 7 days) ---
    @Modifying
    @Transactional
    @Query("update InvitationEntity i " +
            "set i.status = 'EXPIRED' " +
            "where i.status = 'PENDING' and i.createdAt < :cutoff")
    int expireOlderThan(@Param("cutoff") OffsetDateTime cutoff);
}
