package com.digitopia.orgservice.service;

import com.digitopia.orgservice.client.UserClient;
import com.digitopia.orgservice.domain.OrganizationEntity;
import com.digitopia.orgservice.exception.ResourceNotFoundException;
import com.digitopia.orgservice.repo.OrganizationRepository;
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
class OrganizationServiceTest {

    @Mock private OrganizationRepository repo;
    @Mock private UserClient userClient;
    @InjectMocks private OrganizationService svc;

    @Test
    void create_shouldValidateOwner_andNormalize_andSave() {
        OrganizationEntity o = new OrganizationEntity();
        o.setName("Çağrı Öztürk A.Ş.");
        o.setOwnerId(UUID.randomUUID());

        when(repo.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());
        doReturn(new com.digitopia.orgservice.client.dto.UserDto(o.getOwnerId(), "owner@example.com", "ACTIVE", "Owner"))
                .when(userClient).getById(o.getOwnerId());
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        OrganizationEntity saved = svc.create(o);

        assertEquals("cagriozturkas", saved.getNormalizedName());
        verify(userClient).getById(o.getOwnerId());
        verify(repo).save(any());
    }

    @Test
    void create_shouldThrow_ifNameExists() {
        OrganizationEntity o = new OrganizationEntity();
        o.setName("Digitopia");
        o.setOwnerId(UUID.randomUUID());

        when(repo.findByNameIgnoreCase("Digitopia")).thenReturn(Optional.of(new OrganizationEntity()));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> svc.create(o));
        assertTrue(ex.getMessage().toLowerCase().contains("exists"));
    }

    @Test
    void update_shouldThrow_ifNotFound() {
        OrganizationEntity o = new OrganizationEntity();
        o.setId(UUID.randomUUID());
        o.setOwnerId(UUID.randomUUID());
        when(repo.findById(o.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> svc.update(o));
    }

    @Test
    void search_delegatesToRepo() {
        when(repo.findByNormalizedNameContainingIgnoreCase(eq("dig"), any()))
                .thenReturn(new PageImpl<>(List.of(new OrganizationEntity())));
        var page = svc.search("dig", PageRequest.of(0, 10));
        assertEquals(1, page.getTotalElements());
    }

    @Test
    void get_returnsEntity_or404() {
        UUID id = UUID.randomUUID();
        OrganizationEntity e = new OrganizationEntity();
        e.setId(id);
        when(repo.findById(id)).thenReturn(Optional.of(e));

        OrganizationEntity found = svc.get(id);
        assertEquals(id, found.getId());

        when(repo.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> svc.get(id));
    }
}
