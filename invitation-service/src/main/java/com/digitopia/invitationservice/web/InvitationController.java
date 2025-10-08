package com.digitopia.invitationservice.web;

import com.digitopia.invitationservice.domain.InvitationEntity;
import com.digitopia.invitationservice.service.InvitationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invitations")
public class InvitationController {
    private final InvitationService svc;
    public InvitationController(InvitationService svc){ this.svc = svc; }

    @PostMapping
    public ResponseEntity<InvitationEntity> create(@RequestBody InvitationEntity body){
        return ResponseEntity.ok(svc.create(body));
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<InvitationEntity> accept(@PathVariable UUID id){
        return ResponseEntity.ok(svc.updateStatus(id, "ACCEPTED"));
    }

    @PatchMapping("/{id}/decline")
    public ResponseEntity<InvitationEntity> decline(@PathVariable UUID id){
        return ResponseEntity.ok(svc.updateStatus(id, "DECLINED"));
    }

    @GetMapping("/by-org/{orgId}")
    public List<InvitationEntity> byOrg(@PathVariable UUID orgId){
        return svc.getByOrganization(orgId);
    }

    @GetMapping("/by-user/{userId}")
    public List<InvitationEntity> byUser(@PathVariable UUID userId){
        return svc.getByUser(userId);
    }

    @GetMapping("/healtz")
    public String healtz(){ return "ok"; }
}
