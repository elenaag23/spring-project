package com.example.spring_project.tests.transaction;

import com.example.spring_project.controllers.connectors.StartChargingRequest;
import com.example.spring_project.controllers.connectors.StopRequest;
import com.example.spring_project.enums.ConnectorStatus;
import com.example.spring_project.models.entities.Connector;
import com.example.spring_project.models.entities.Station;
import com.example.spring_project.models.entities.Transaction;
import com.example.spring_project.models.entities.User;
import com.example.spring_project.repositories.ConnectorRepository;
import com.example.spring_project.repositories.TransactionRepository;
import com.example.spring_project.repositories.UserRepository;
import com.example.spring_project.services.TransactionService;
import com.example.spring_project.services.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConnectorRepository connectorRepository;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTransaction_Success() {
        // Arrange
        String username = "test@example.com";
        StartChargingRequest request = new StartChargingRequest();
        request.setStationId(1);
        request.setConnectorId(1);
        request.setRevenue(100.0);

        User user = new User();
        user.setEmail(username);

        Connector connector = new Connector();
        connector.setPricePerKw(10.0);

        Transaction transaction = new Transaction(LocalDateTime.now(), connector, 100.0, 10.0, user);

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));
        when(connectorRepository.findByConnectorIdAndStationId(1, 1)).thenReturn(Optional.of(connector));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // Act
        Transaction result = transactionService.createTransaction(username, request);

        // Assert
        assertNotNull(result);
        assertEquals(100.0, result.getRevenue());
        assertEquals(10.0, result.getAmount());
        verify(walletService, times(1)).addPoints(user, 10.0);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void createTransaction_UserNotFound() {
        // Arrange
        String username = "nonexistent@example.com";
        StartChargingRequest request = new StartChargingRequest();
        request.setStationId(1);
        request.setConnectorId(1);
        request.setRevenue(100.0);

        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> transactionService.createTransaction(username, request));
    }

    @Test
    void stopTransaction_Success() {
        // Arrange
        StopRequest request = new StopRequest();
        request.setTransactionId(1);

        Connector connector = new Connector();
        Station station = new Station();
        station.setStationId(1);
        connector.setConnectorId(1);
        connector.setStation(station);
        connector.setChargingStatus(ConnectorStatus.CHARGING.toString());

        Transaction transaction = new Transaction();
        transaction.setTransactionId(1);
        transaction.setConnector(connector);

        when(transactionRepository.findByTransactionId(1)).thenReturn(Optional.of(transaction));
        when(connectorRepository.findByConnectorIdAndStationId(1, 1)).thenReturn(Optional.of(connector));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // Act
        Transaction result = transactionService.stopTransaction(request);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getStopTime());
        assertEquals(ConnectorStatus.FREE.toString(), connector.getChargingStatus());
        verify(connectorRepository, times(1)).save(connector);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void stopTransaction_TransactionNotFound() {
        // Arrange
        StopRequest request = new StopRequest();
        request.setTransactionId(1);

        when(transactionRepository.findByTransactionId(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> transactionService.stopTransaction(request));
    }

    @Test
    void getUserTransactions_Success() {
        // Arrange
        String username = "test@example.com";

        User user = new User();
        user.setEmail(username);
        Transaction transaction = new Transaction();
        user.setTransactions(Collections.singletonList(transaction));

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));

        // Act
        List<Transaction> result = transactionService.getUserTransactions(username);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findByEmail(username);
    }

    @Test
    void getUserTransactions_UserNotFound() {
        // Arrange
        String username = "nonexistent@example.com";

        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> transactionService.getUserTransactions(username));
    }
}
