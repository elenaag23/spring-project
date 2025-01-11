package com.example.spring_project.controllers.users;

import com.example.spring_project.security.JwtUtil;
import com.example.spring_project.services.UserService;
import com.example.spring_project.utils.ResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/profile")
@Tag(name = "User Management", description = "Endpoints for managing user profiles")
public class GetProfileController {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public GetProfileController(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Operation(summary = "Get user profile", description = "Retrieves the profile of the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfileResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<ResponseTemplate<ProfileResponse>> getProfile(
            @Parameter(description = "Authorization token")
            @RequestHeader(value = "Authorization") String token) {

        token = token.substring(7);
        String username = jwtUtil.extractUsername(token);

        ProfileResponse response = userService.getUserProfile(username);

        return new ResponseEntity<>(new ResponseTemplate<>("Profile retrieved successfully", response), HttpStatus.OK);
    }
}
