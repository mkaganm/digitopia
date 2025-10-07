package com.digitopia.userservice.web;

import com.digitopia.userservice.domain.UserEntity;
import com.digitopia.userservice.repo.UserRepository;
import com.digitopia.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService svc;
    private final UserRepository repo;

    public UserController(UserService svc, UserRepository repo) {
        this.svc = svc; this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<UserEntity> create(@Valid @RequestBody UserEntity body){
        return ResponseEntity.ok(svc.create(body));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> get(@PathVariable UUID id){
        return repo.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-email")
    public ResponseEntity<UserEntity> byEmail(@RequestParam String email){
        return repo.findByEmailIgnoreCase(email)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public Page<UserEntity> search(@RequestParam String name,
                                   @RequestParam(defaultValue="0") int page,
                                   @RequestParam(defaultValue="20") int size){
        return svc.searchByName(name, PageRequest.of(page, size));
    }

    @GetMapping("/healtz")
    public String healtz(){ return "ok"; }
}
