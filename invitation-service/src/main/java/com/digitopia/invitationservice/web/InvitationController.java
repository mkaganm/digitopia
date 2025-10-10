package com.digitopia.invitationservice.web;

import com.digitopia.invitationservice.domain.InvitationEntity;
import com.digitopia.invitationservice.service.InvitationService;
import com.digitopia.invitationservice.web.dto.CreateInvitationDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URI;
import java.util.List;
import java.util.UUID;


@Tag(name = "Invitations", description = "Invitation management endpoints")
@RestController
@RequestMapping("/api/v1/invitations")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService svc;

    @PostMapping
    public ResponseEntity<InvitationEntity> create(@Valid @RequestBody CreateInvitationDto dto) {
        InvitationEntity inv = new InvitationEntity();
        inv.setOrganizationId(dto.organizationId());
        inv.setInvitedUserId(dto.invitedUserId());
        inv.setMessage(dto.message());

        InvitationEntity saved = svc.create(inv);

        // Return 201 Created + Location header
        URI location = URI.create("/api/v1/invitations/" + saved.getId());
        return ResponseEntity.created(location).body(saved);
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<InvitationEntity> accept(@PathVariable UUID id) {
        return ResponseEntity.ok(svc.updateStatus(id, "ACCEPTED"));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<InvitationEntity> reject(@PathVariable UUID id) {
        return ResponseEntity.ok(svc.updateStatus(id, "REJECTED"));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<InvitationEntity> updateStatus(
            @PathVariable UUID id,
            @RequestParam String status
    ) {
        return ResponseEntity.ok(svc.updateStatus(id, status));
    }

    @GetMapping("/by-org/{orgId}")
    public List<InvitationEntity> byOrg(@PathVariable UUID orgId) {
        return svc.getByOrganization(orgId);
    }

    @GetMapping("/by-user/{userId}")
    public List<InvitationEntity> byUser(@PathVariable UUID userId) {
        return svc.getByUser(userId);
    }

    @GetMapping("/healtz")
    public String healtz() {
        return "ok";
    }
}
