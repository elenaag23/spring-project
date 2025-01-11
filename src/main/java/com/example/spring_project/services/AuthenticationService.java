package com.example.spring_project.services;

import com.example.spring_project.controllers.authentication.LoginRequest;
import com.example.spring_project.controllers.authentication.RegisterRequest;
import com.example.spring_project.models.entities.User;
import com.example.spring_project.models.entities.Wallet;
import com.example.spring_project.repositories.UserRepository;
import com.example.spring_project.repositories.WalletRepository;
import com.example.spring_project.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    private final WalletRepository walletRepository;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager, WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.walletRepository = walletRepository;
    }

    public String register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already registered.");
        }

        if (request.getEmail() == null || request.getEmail().isEmpty() ||
                request.getPassword() == null || request.getPassword().isEmpty() ||
                request.getName() == null || request.getName().isEmpty()) {
            throw new IllegalArgumentException("Invalid registration data.");
        }

        User user = new User(request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getName());
        User saved = userRepository.save(user);

        Wallet wallet = new Wallet(saved, 0);
        walletRepository.save(wallet);

        String token = jwtUtil.generateToken(user);

        return token;

    }

    public String login(LoginRequest request) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(), request.getPassword()
            ));
        } catch(Exception e){
            throw new IllegalArgumentException("Invalid email or password.");
        }


        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                new IllegalArgumentException("Invalid email or password.") // Change here
        );
        String token = jwtUtil.generateToken(user);
        return token;
    }
}
