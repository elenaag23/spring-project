package com.example.spring_project.controllers.users;

import com.example.spring_project.services.UserService;
import com.example.spring_project.utils.ResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/edit")
@Tag(name = "User Management", description = "Endpoints for editing user details")
public class EditController {

    private final UserService userService;

    public EditController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Edit user details", description = "Allows a user to edit their details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edit successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EditResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping
    public ResponseEntity<ResponseTemplate<EditResponse>> edit(
            @RequestBody @Valid EditRequest request) {

        EditResponse editResponse = userService.edit(request);
        return new ResponseEntity<>(new ResponseTemplate<>("Edit successful", editResponse), HttpStatus.OK);
    }
}
