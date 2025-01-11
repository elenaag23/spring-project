package com.example.spring_project.tests.reservation;

import com.example.spring_project.controllers.connectors.ReserveRequest;
import com.example.spring_project.models.entities.Reservation;
import com.example.spring_project.models.entities.User;
import com.example.spring_project.repositories.ReservationRepository;
import com.example.spring_project.repositories.UserRepository;
import com.example.spring_project.services.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createReservation_Success() {
        // Arrange
        String username = "test@example.com";
        ReserveRequest reserveRequest = new ReserveRequest();
        reserveRequest.setStationId(1);
        reserveRequest.setConnectorId(1);

        User user = new User();
        user.setEmail(username);

        Reservation expectedReservation = new Reservation(
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user,
                reserveRequest.getStationId(),
                reserveRequest.getConnectorId()
        );

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(expectedReservation);

        // Act
        Reservation actualReservation = reservationService.createReservation(username, reserveRequest);

        // Assert
        assertNotNull(actualReservation);
        assertEquals(expectedReservation.getStationId(), actualReservation.getStationId());
        assertEquals(expectedReservation.getConnectorId(), actualReservation.getConnectorId());
        assertEquals(expectedReservation.getUser(), actualReservation.getUser());
        assertNotNull(actualReservation.getTimestamp());
        assertNotNull(actualReservation.getExpirationTime());
        verify(userRepository, times(1)).findByEmail(username);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void createReservation_UserNotFound() {
        // Arrange
        String username = "nonexistent@example.com";
        ReserveRequest reserveRequest = new ReserveRequest();
        reserveRequest.setStationId(1);
        reserveRequest.setConnectorId(2);

        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> reservationService.createReservation(username, reserveRequest));
        verify(userRepository, times(1)).findByEmail(username);
        verify(reservationRepository, never()).save(any(Reservation.class));
    }
}
