package com.example.spring_project.services;

import com.example.spring_project.controllers.users.EditRequest;
import com.example.spring_project.controllers.users.EditResponse;
import com.example.spring_project.controllers.users.ProfileResponse;
import com.example.spring_project.models.entities.User;
import com.example.spring_project.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public EditResponse edit(@Valid EditRequest request) {
        int rowsUpdated = userRepository.updateUser(request.getName(), request.getEmail());
        if (rowsUpdated == 0) {
            throw new IllegalArgumentException("No user found with email: " + request.getEmail());
        }

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        return new EditResponse(user.getEmail(), user.getName());
    }

    public ProfileResponse getUserProfile(String username) {
        User user = userRepository.findByEmail(username).orElseThrow();

        ProfileResponse profileResponse = new ProfileResponse(user.getName(), user.getEmail(), user.getWallet().getFidelityPoints());

        return profileResponse;
    }
}
