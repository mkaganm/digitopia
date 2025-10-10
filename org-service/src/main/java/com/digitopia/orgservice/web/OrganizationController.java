package com.digitopia.orgservice.web;

import com.digitopia.orgservice.domain.OrganizationEntity;
import com.digitopia.orgservice.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URI;
import java.util.UUID;

@Tag(name = "Organizations", description = "Organization management endpoints")
@RestController
@RequestMapping("/api/v1/orgs")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService svc;


    @PostMapping
    public ResponseEntity<OrganizationEntity> create(@Valid @RequestBody OrganizationEntity body) {
        OrganizationEntity saved = svc.create(body);
        return ResponseEntity.created(URI.create("/api/v1/orgs/" + saved.getId()))
                .body(saved);
    }

    @PutMapping
    public ResponseEntity<OrganizationEntity> update(@Valid @RequestBody OrganizationEntity body) {
        return ResponseEntity.ok(svc.update(body));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationEntity> get(@PathVariable UUID id) {
        return ResponseEntity.ok(svc.get(id));
    }

    @GetMapping("/search")
    public Page<OrganizationEntity> search(@RequestParam String q,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "20") int size) {
        return svc.search(q, PageRequest.of(page, size));
    }

    @GetMapping("/healtz")
    public String healtz() {
        return "ok";
    }
}
