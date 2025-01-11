package com.example.spring_project.tests.connector;


import com.example.spring_project.controllers.connectors.AddRequest;
import com.example.spring_project.controllers.connectors.ReserveRequest;
import com.example.spring_project.controllers.connectors.StartChargingRequest;
import com.example.spring_project.enums.ConnectorStatus;
import com.example.spring_project.enums.ConnectorType;
import com.example.spring_project.enums.CurrentType;
import com.example.spring_project.models.entities.Connector;
import com.example.spring_project.models.entities.Reservation;
import com.example.spring_project.models.entities.Station;
import com.example.spring_project.models.entities.User;
import com.example.spring_project.repositories.ConnectorRepository;
import com.example.spring_project.repositories.ReservationRepository;
import com.example.spring_project.repositories.StationRepository;
import com.example.spring_project.repositories.UserRepository;
import com.example.spring_project.services.ConnectorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConnectorServiceTest {

    @Mock
    private ConnectorRepository connectorRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReservationRepository reservationRepository;

    private ConnectorService connectorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        connectorService = new ConnectorService(connectorRepository, stationRepository, userRepository, reservationRepository);
    }

    @Test
    void addConnector_Success() {
        // Arrange
        AddRequest request = new AddRequest();
        request.setStationId(1);
        request.setCurrentType(CurrentType.valueOf("AC"));
        request.setConnectorType(ConnectorType.valueOf("TYPE2"));
        request.setMaxPower(22.0);
        request.setPrice(0.25);
        request.setStatus(ConnectorStatus.valueOf("FREE"));

        Station station = new Station();
        when(stationRepository.findById(1)).thenReturn(Optional.of(station));

        Connector connector = new Connector();
        when(connectorRepository.save(any(Connector.class))).thenReturn(connector);

        // Act
        Connector result = connectorService.addConnector(request);

        // Assert
        assertNotNull(result);
        verify(stationRepository, times(1)).findById(1);
        verify(connectorRepository, times(1)).save(any(Connector.class));
    }

    @Test
    void reserveConnector_Success() {
        // Arrange
        ReserveRequest request = new ReserveRequest();
        request.setStationId(1);
        request.setConnectorId(1);

        Connector connector = new Connector();
        connector.setChargingStatus(ConnectorStatus.FREE.toString());
        when(connectorRepository.findByConnectorIdAndStationId(1, 1)).thenReturn(Optional.of(connector));

        Connector updatedConnector = new Connector();
        updatedConnector.setChargingStatus(ConnectorStatus.RESERVED.toString());
        when(connectorRepository.save(any(Connector.class))).thenReturn(updatedConnector);

        // Act
        Connector result = connectorService.reserveConnector(request);

        // Assert
        assertNotNull(result);
        assertEquals(ConnectorStatus.RESERVED.toString(), result.getChargingStatus());
        verify(connectorRepository, times(1)).findByConnectorIdAndStationId(1, 1);
        verify(connectorRepository, times(1)).save(any(Connector.class));
    }

    @Test
    void reserveConnector_ConnectorUnavailable() {
        // Arrange
        ReserveRequest request = new ReserveRequest();
        request.setStationId(1);
        request.setConnectorId(1);

        Connector connector = new Connector();
        connector.setChargingStatus(ConnectorStatus.CHARGING.toString());
        when(connectorRepository.findByConnectorIdAndStationId(1, 1)).thenReturn(Optional.of(connector));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> connectorService.reserveConnector(request));
        assertEquals("Connector unavailable", exception.getMessage());
        verify(connectorRepository, times(1)).findByConnectorIdAndStationId(1, 1);
    }

    @Test
    void startCharging_Success_Reserved() {
        // Arrange
        String username = "test@example.com";
        StartChargingRequest request = new StartChargingRequest();
        request.setStationId(1);
        request.setConnectorId(1);

        User user = new User();
        user.setUserId(1);
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));

        Connector connector = new Connector();
        connector.setChargingStatus(ConnectorStatus.RESERVED.toString());
        when(connectorRepository.findByConnectorIdAndStationId(1, 1)).thenReturn(Optional.of(connector));

        Reservation reservation = new Reservation();
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);
        user.setReservations(reservations);
        LocalDateTime testTimestamp = LocalDateTime.now();
        when(reservationRepository.findByConnectorIdAndStationIdAndUserId(1, 1, 1, testTimestamp))
                .thenReturn(Optional.of(reservation));

        Connector updatedConnector = new Connector();
        updatedConnector.setChargingStatus(ConnectorStatus.CHARGING.toString());
        when(connectorRepository.save(any(Connector.class))).thenReturn(updatedConnector);

        // Act
        Connector result = connectorService.startCharging(username, request);

        // Assert
        assertNotNull(result);
        assertEquals(ConnectorStatus.CHARGING.toString(), result.getChargingStatus());
        verify(userRepository, times(1)).findByEmail(username);
        verify(connectorRepository, times(1)).findByConnectorIdAndStationId(1, 1);
        verify(reservationRepository, times(1)).findByConnectorIdAndStationIdAndUserId(1, 1, 1, testTimestamp);
        verify(connectorRepository, times(1)).save(any(Connector.class));
    }

    @Test
    void startCharging_Success_Free() {
        // Arrange
        String username = "test@example.com";
        StartChargingRequest request = new StartChargingRequest();
        request.setStationId(1);
        request.setConnectorId(1);

        User user = new User();
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));

        Connector connector = new Connector();
        connector.setChargingStatus(ConnectorStatus.FREE.toString());
        when(connectorRepository.findByConnectorIdAndStationId(1, 1)).thenReturn(Optional.of(connector));

        Connector updatedConnector = new Connector();
        updatedConnector.setChargingStatus(ConnectorStatus.CHARGING.toString());
        when(connectorRepository.save(any(Connector.class))).thenReturn(updatedConnector);

        // Act
        Connector result = connectorService.startCharging(username, request);

        // Assert
        assertNotNull(result);
        assertEquals(ConnectorStatus.CHARGING.toString(), result.getChargingStatus());
        verify(userRepository, times(1)).findByEmail(username);
        verify(connectorRepository, times(1)).findByConnectorIdAndStationId(1, 1);
        verify(connectorRepository, times(1)).save(any(Connector.class));
    }
}
