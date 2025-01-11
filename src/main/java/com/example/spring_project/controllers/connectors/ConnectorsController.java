package com.example.spring_project.controllers.connectors;

import com.example.spring_project.models.entities.Connector;
import com.example.spring_project.models.entities.Reservation;
import com.example.spring_project.models.entities.Transaction;
import com.example.spring_project.security.JwtUtil;
import com.example.spring_project.services.ConnectorService;
import com.example.spring_project.services.ReservationService;
import com.example.spring_project.services.TransactionService;
import com.example.spring_project.utils.ResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/connector")
@Tag(name = "Connectors", description = "Endpoints for managing connectors")
public class ConnectorsController {

    private static final Logger log = LoggerFactory.getLogger(ConnectorsController.class);
    private final ConnectorService connectorService;
    private final ReservationService reservationService;
    private final JwtUtil jwtUtil;
    private final TransactionService transactionService;

    public ConnectorsController(ConnectorService connectorService, ReservationService reservationService, JwtUtil jwtUtil, TransactionService transactionService) {
        this.connectorService = connectorService;
        this.reservationService = reservationService;
        this.jwtUtil = jwtUtil;
        this.transactionService = transactionService;
    }

    @Operation(summary = "Add a new connector", description = "Adds a new connector to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connector added successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Connector.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/add")
    public ResponseEntity<ResponseTemplate<Connector>> addConnector(@RequestBody @Valid AddRequest request) {
        Connector added = connectorService.addConnector(request);
        return new ResponseEntity<>(new ResponseTemplate<>("Connector added successfully", added), HttpStatus.OK);
    }

    @Operation(summary = "Reserve a connector", description = "Reserves a connector for a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connector reserved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/reserve")
    public ResponseEntity<ResponseTemplate<ReservationResponse>> reserveConnector(
            @Parameter(description = "Authorization token") @RequestHeader("Authorization") String token,
            @RequestBody @Valid ReserveRequest request) {

        token = token.substring(7);
        String username = jwtUtil.extractUsername(token);
        Connector reserved = connectorService.reserveConnector(request);
        Reservation reservation = reservationService.createReservation(username, request);

        ReservationResponse reservationResponse = new ReservationResponse(
                username, reserved.getConnectorId(), reserved.getStation().getStationId(),
                reservation.getTimestamp(), reservation.getExpirationTime());

        return new ResponseEntity<>(new ResponseTemplate<>("Connector reserved successfully", reservationResponse), HttpStatus.OK);
    }

    @Operation(summary = "Start charging", description = "Starts a charging session for a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Charging started successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Transaction.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/startCharging")
    public ResponseEntity<ResponseTemplate<Transaction>> startCharging(
            @Parameter(description = "Authorization token") @RequestHeader("Authorization") String token,
            @RequestBody @Valid StartChargingRequest request) {

        token = token.substring(7);
        String username = jwtUtil.extractUsername(token);
        Connector started = connectorService.startCharging(username, request);
        Transaction transaction = transactionService.createTransaction(username, request);

        return new ResponseEntity<>(new ResponseTemplate<>("Charging started successfully", transaction), HttpStatus.OK);
    }

    @Operation(summary = "Stop charging", description = "Stops a charging session for a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Charging stopped successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Transaction.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/stopCharging")
    public ResponseEntity<ResponseTemplate<Transaction>> stopCharging(
            @Parameter(description = "Authorization token") @RequestHeader("Authorization") String token,
            @RequestBody @Valid StopRequest request) {

        token = token.substring(7);
        log.info("stop charging request: " + request);
        String username = jwtUtil.extractUsername(token);
        Transaction started = transactionService.stopTransaction(request);

        return new ResponseEntity<>(new ResponseTemplate<>("Charging stopped successfully", started), HttpStatus.OK);
    }
}
