package com.example.spring_project.controllers;

import com.example.spring_project.models.entities.User;
import com.example.spring_project.repositories.UserRepository;
import com.example.spring_project.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = new User(request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getName());
        userRepository.save(user);

        String token = jwtUtil.generateToken(user);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse(token);

        return authenticationResponse;

    }

    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()
        ));

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtUtil.generateToken(user);
        return new AuthenticationResponse(token);
    }
}
