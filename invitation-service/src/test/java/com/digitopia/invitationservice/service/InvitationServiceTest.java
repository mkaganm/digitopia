package com.digitopia.invitationservice.service;

import com.digitopia.invitationservice.domain.InvitationEntity;
import com.digitopia.invitationservice.exception.ResourceNotFoundException;
import com.digitopia.invitationservice.repo.InvitationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvitationServiceTest {

    @Mock private InvitationRepository repo;
    @Mock private RabbitTemplate rabbit;
    @Mock private TopicExchange exchange;

    // Service has final UserClient & OrgClient fields → mock them
    @Mock private com.digitopia.invitationservice.client.UserClient userClient;
    @Mock private com.digitopia.invitationservice.client.OrgClient orgClient;

    @InjectMocks private InvitationService svc;

    private InvitationEntity inv;

    @BeforeEach
    void setup() {
        inv = new InvitationEntity();
        inv.setOrganizationId(UUID.randomUUID());
        inv.setInvitedUserId(UUID.randomUUID());
        inv.setStatus("PENDING");
    }

    @Test
    void create_shouldSave_andPublishCreatedEvent() {
        when(repo.findByOrganizationIdAndInvitedUserIdAndStatus(
                inv.getOrganizationId(), inv.getInvitedUserId(), "PENDING"
        )).thenReturn(Optional.empty());

        // external validations (return values not used here)
        when(userClient.getById(inv.getInvitedUserId())).thenReturn(null);
        when(orgClient.getById(inv.getOrganizationId())).thenReturn(null);

        when(repo.save(any())).thenAnswer(i -> {
            InvitationEntity saved = i.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        when(exchange.getName()).thenReturn("digitopia.exchange");

        InvitationEntity saved = svc.create(inv);

        assertNotNull(saved.getId());
        verify(repo).save(any());
        verify(rabbit).convertAndSend("digitopia.exchange", "invitation.created", saved.getId().toString());
        verify(userClient).getById(inv.getInvitedUserId());
        verify(orgClient).getById(inv.getOrganizationId());
    }

    @Test
    void create_shouldThrow_ifPendingExists() {
        when(repo.findByOrganizationIdAndInvitedUserIdAndStatus(
                inv.getOrganizationId(), inv.getInvitedUserId(), "PENDING"
        )).thenReturn(Optional.of(new InvitationEntity()));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> svc.create(inv));
        assertTrue(ex.getMessage().toLowerCase().contains("pending"));

        verifyNoInteractions(rabbit);
        verifyNoInteractions(userClient);
        verifyNoInteractions(orgClient);
    }

    @Test
    void updateStatus_shouldSave_andPublishStatusChanged() {
        UUID id = UUID.randomUUID();
        inv.setId(id);

        when(repo.findById(id)).thenReturn(Optional.of(inv));
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));
        when(exchange.getName()).thenReturn("digitopia.exchange");

        InvitationEntity updated = svc.updateStatus(id, "ACCEPTED");

        assertEquals("ACCEPTED", updated.getStatus());
        verify(rabbit).convertAndSend("digitopia.exchange", "invitation.status.changed", id + ":" + "ACCEPTED");
    }

    @Test
    void updateStatus_should404_ifNotFound() {
        UUID id = UUID.randomUUID();
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> svc.updateStatus(id, "ACCEPTED"));
        verifyNoInteractions(rabbit);
    }
}
