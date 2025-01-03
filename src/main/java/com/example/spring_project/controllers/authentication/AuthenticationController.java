package com.example.spring_project.controllers.authentication;

import com.example.spring_project.services.AuthenticationService;
import com.example.spring_project.utils.ResponseTemplate;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseTemplate<String>> register(@RequestBody @Valid RegisterRequest request){

        String token = authenticationService.register(request);

        return new ResponseEntity<>(new ResponseTemplate<>
                ("Register successful", token), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseTemplate<String>> login(@RequestBody @Valid LoginRequest request){
        String token = authenticationService.login(request);
        return new ResponseEntity<>(new ResponseTemplate<>
                ("Login successful", token),
                HttpStatus.OK);


    }
}
