package com.digitopia.userservice.service;

import com.digitopia.userservice.domain.UserEntity;
import com.digitopia.userservice.repo.UserRepository;
import com.digitopia.userservice.user.api.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository repo;
    @InjectMocks private UserService svc;

    private UserEntity u;

    @BeforeEach
    void setup() {
        u = new UserEntity();
        u.setId(UUID.randomUUID());
        u.setEmail("alice@example.com");
        u.setFullName("Alice Doe");
        u.setStatus("ACTIVE");
    }

    @Test
    void create_shouldNormalizeName_andSave() {
        when(repo.findByEmailIgnoreCase("alice@example.com")).thenReturn(Optional.empty());
        when(repo.save(any(UserEntity.class))).thenAnswer(i -> i.getArgument(0));

        UserEntity saved = svc.create(u);

        assertEquals("alice@example.com", saved.getEmail());
        assertEquals("alicedoe", saved.getNormalizedName()); // only a-z0-9
        verify(repo).save(any(UserEntity.class));
    }

    @Test
    void create_shouldThrow_ifEmailExists() {
        when(repo.findByEmailIgnoreCase("alice@example.com")).thenReturn(Optional.of(u));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> svc.create(u));
        assertTrue(ex.getMessage().toLowerCase().contains("email"));
    }

    @Test
    void searchByName_delegatesToRepo() {
        when(repo.findByNormalizedNameContainingIgnoreCase(eq("ali"), any()))
                .thenReturn(new PageImpl<>(List.of(u)));
        var page = svc.searchByName("ali", PageRequest.of(0, 10));
        assertEquals(1, page.getTotalElements());
    }

    @Test
    void getById_mapsToDto() {
        UUID id = u.getId();
        when(repo.findById(id)).thenReturn(Optional.of(u));

        UserDto dto = svc.getById(id);

        assertEquals(id, dto.id());
        assertEquals("alice@example.com", dto.email());
    }
}
