package com.example.spring_project.tests.wallet;

import com.example.spring_project.models.entities.Transaction;
import com.example.spring_project.models.entities.User;
import com.example.spring_project.models.entities.Wallet;
import com.example.spring_project.repositories.WalletRepository;
import com.example.spring_project.services.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addPoints_Success() {
        // Arrange
        User user = new User();
        Wallet wallet = new Wallet();
        wallet.setFidelityPoints(50);
        user.setWallet(wallet);

        user.setTransactions(new ArrayList<>());
        user.getTransactions().add(new Transaction());

        double kwCharged = 20.0;
        int expectedPoints = 50 + (int) (kwCharged + 0.1 * user.getTransactions().size());

        when(walletRepository.save(wallet)).thenReturn(wallet);

        // Act
        walletService.addPoints(user, kwCharged);

        // Assert
        assertEquals(expectedPoints, wallet.getFidelityPoints());
        verify(walletRepository, times(1)).save(wallet);
    }

    @Test
    void addPoints_NoTransactions() {
        // Arrange
        User user = new User();
        Wallet wallet = new Wallet();
        wallet.setFidelityPoints(10);
        user.setWallet(wallet);

        user.setTransactions(new ArrayList<>()); // No transactions

        double kwCharged = 30.0;
        int expectedPoints = 10 + (int) (kwCharged + 0.1 * user.getTransactions().size());

        when(walletRepository.save(wallet)).thenReturn(wallet);

        // Act
        walletService.addPoints(user, kwCharged);

        // Assert
        assertEquals(expectedPoints, wallet.getFidelityPoints());
        verify(walletRepository, times(1)).save(wallet);
    }

    @Test
    void addPoints_NegativeKwCharged() {
        // Arrange
        User user = new User();
        Wallet wallet = new Wallet();
        wallet.setFidelityPoints(30);
        user.setWallet(wallet);

        user.setTransactions(new ArrayList<>());

        double kwCharged = -10.0;
        int expectedPoints = 30 + (int) (kwCharged + 0.1 * user.getTransactions().size());

        when(walletRepository.save(wallet)).thenReturn(wallet);

        // Act
        walletService.addPoints(user, kwCharged);

        // Assert
        assertEquals(expectedPoints, wallet.getFidelityPoints());
        verify(walletRepository, times(1)).save(wallet);
    }
}
