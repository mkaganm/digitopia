package com.digitopia.orgservice.repo;

import com.digitopia.orgservice.domain.OrganizationMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface MembershipRepository extends JpaRepository<OrganizationMembership, UUID> {

    @Transactional
    @Modifying
    @Query(value = """
        INSERT INTO organization_memberships (id, organization_id, user_id, joined_at)
        VALUES (:id, :orgId, :userId, :joinedAt)
        ON CONFLICT (organization_id, user_id) DO NOTHING
        """, nativeQuery = true)
    void upsert(@Param("id") UUID id,
                @Param("orgId") UUID orgId,
                @Param("userId") UUID userId,
                @Param("joinedAt") OffsetDateTime joinedAt);
}
