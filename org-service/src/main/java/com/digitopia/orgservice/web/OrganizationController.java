package com.digitopia.orgservice.web;

import com.digitopia.orgservice.domain.OrganizationEntity;
import com.digitopia.orgservice.service.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orgs")
public class OrganizationController {
    private final OrganizationService svc;
    public OrganizationController(OrganizationService svc){ this.svc = svc; }

    @PostMapping
    public ResponseEntity<OrganizationEntity> create(@Valid @RequestBody OrganizationEntity body){
        return ResponseEntity.ok(svc.create(body));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationEntity> get(@PathVariable UUID id){
        return svc.get(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public Page<OrganizationEntity> search(@RequestParam String name,
                                           @RequestParam(defaultValue="0") int page,
                                           @RequestParam(defaultValue="20") int size){
        return svc.search(name, PageRequest.of(page, size));
    }

    @GetMapping("/healtz")
    public String healtz(){ return "ok"; }
}
