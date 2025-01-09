package com.example.spring_project.controllers.users;

import com.example.spring_project.security.JwtUtil;
import com.example.spring_project.services.UserService;
import com.example.spring_project.utils.ResponseTemplate;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/profile")
public class GetProfileController {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public GetProfileController(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ResponseTemplate<ProfileResponse>> getProfile(@RequestHeader(value = "Authorization") String token){

        token = token.substring(7);
        String username = jwtUtil.extractUsername(token);

        ProfileResponse response = userService.getUserProfile(username);

        return new ResponseEntity<>(new ResponseTemplate<>
                ("Edit successful", response), HttpStatus.OK);
    }
}
