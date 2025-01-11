package com.example.spring_project.controllers.stations;

import com.example.spring_project.models.entities.Station;
import com.example.spring_project.services.StationService;
import com.example.spring_project.utils.ResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/station")
@Tag(name = "Stations", description = "Endpoints for managing stations")
public class StationsController {

    private final StationService stationService;

    public StationsController(StationService stationService) {
        this.stationService = stationService;
    }

    @Operation(summary = "Add a new station", description = "Adds a new station to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Station added successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Station.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/add")
    public ResponseEntity<ResponseTemplate<Station>> addStation(
            @RequestBody @Valid AddRequest request) {

        Station added = stationService.addStation(request);
        return new ResponseEntity<>(new ResponseTemplate<>("Station added successfully", added), HttpStatus.OK);
    }

    @Operation(summary = "Get a list of stations", description = "Retrieves a list of stations. Optionally filters by location.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stations retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Station.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/get")
    public ResponseEntity<ResponseTemplate<List<Station>>> getStations(
            @Parameter(description = "Optional filter by location")
            @RequestBody(required = false) GetByLocationRequest getByLocationRequest) {

        List<Station> stations = stationService.getStations(getByLocationRequest);
        return new ResponseEntity<>(new ResponseTemplate<>("Stations list retrieved successfully", stations), HttpStatus.OK);
    }
}
