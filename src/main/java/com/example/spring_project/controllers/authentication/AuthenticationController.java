package com.example.spring_project.controllers.authentication;

import com.example.spring_project.services.AuthenticationService;
import com.example.spring_project.utils.ResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Login and register endpoints")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(
            summary = "Register a new user",
            description = "This endpoint registers a new user and returns an authentication token.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Registration successful",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseTemplate.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad request")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<ResponseTemplate<String>> register(@RequestBody @Valid RegisterRequest request) {
        String token = authenticationService.register(request);

        return new ResponseEntity<>(new ResponseTemplate<>(
                "Register successful", token), HttpStatus.OK);
    }

    @Operation(
            summary = "Login an existing user",
            description = "This endpoint logs in a user and returns an authentication token.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login successful",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseTemplate.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid credentials")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<ResponseTemplate<String>> login(@RequestBody @Valid LoginRequest request) {
        String token = authenticationService.login(request);
        return new ResponseEntity<>(new ResponseTemplate<>(
                "Login successful", token),
                HttpStatus.OK);
    }
}
