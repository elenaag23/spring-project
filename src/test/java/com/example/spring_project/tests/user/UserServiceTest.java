package com.example.spring_project.tests.user;

import com.example.spring_project.controllers.users.EditRequest;
import com.example.spring_project.controllers.users.EditResponse;
import com.example.spring_project.controllers.users.ProfileResponse;
import com.example.spring_project.models.entities.User;
import com.example.spring_project.models.entities.Wallet;
import com.example.spring_project.repositories.UserRepository;
import com.example.spring_project.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void edit_Success() {
        // Arrange
        EditRequest request = new EditRequest();
        request.setName("New Name");
        request.setEmail("test@example.com");

        User user = new User();
        user.setName("New Name");
        user.setEmail("test@example.com");

        when(userRepository.updateUser("New Name", "test@example.com")).thenReturn(1);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Act
        EditResponse response = userService.edit(request);

        // Assert
        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
        assertEquals("New Name", response.getName());
        verify(userRepository, times(1)).updateUser("New Name", "test@example.com");
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void edit_UserNotFound() {
        // Arrange
        EditRequest request = new EditRequest();
        request.setName("New Name");
        request.setEmail("nonexistent@example.com");

        when(userRepository.updateUser("New Name", "nonexistent@example.com")).thenReturn(0);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.edit(request));
        verify(userRepository, times(1)).updateUser("New Name", "nonexistent@example.com");
    }

    @Test
    void getUserProfile_Success() {
        // Arrange
        String username = "test@example.com";

        Wallet wallet = new Wallet();
        wallet.setFidelityPoints(100);

        User user = new User();
        user.setName("Test User");
        user.setEmail(username);
        user.setWallet(wallet);

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));

        // Act
        ProfileResponse response = userService.getUserProfile(username);

        // Assert
        assertNotNull(response);
        assertEquals("Test User", response.getName());
        assertEquals(username, response.getEmail());
        assertEquals(100, response.getFidelityPoints());
        verify(userRepository, times(1)).findByEmail(username);
    }

    @Test
    void getUserProfile_UserNotFound() {
        // Arrange
        String username = "nonexistent@example.com";

        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> userService.getUserProfile(username));
        verify(userRepository, times(1)).findByEmail(username);
    }
}
