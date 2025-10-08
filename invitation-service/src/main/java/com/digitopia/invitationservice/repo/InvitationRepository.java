package com.digitopia.invitationservice.repo;

import com.digitopia.invitationservice.domain.InvitationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvitationRepository extends JpaRepository<InvitationEntity, UUID> {
    Optional<InvitationEntity> findByOrganizationIdAndInvitedUserIdAndStatus(UUID orgId, UUID userId, String status);
    List<InvitationEntity> findByOrganizationId(UUID orgId);
    List<InvitationEntity> findByInvitedUserId(UUID userId);
}
