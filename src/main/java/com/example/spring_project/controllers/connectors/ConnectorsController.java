package com.example.spring_project.controllers.connectors;

import com.example.spring_project.controllers.connectors.AddRequest;
import com.example.spring_project.models.entities.Connector;
import com.example.spring_project.models.entities.Reservation;
import com.example.spring_project.security.JwtUtil;
import com.example.spring_project.services.ConnectorService;
import com.example.spring_project.services.ReservationService;
import com.example.spring_project.utils.ResponseTemplate;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/connector")
public class ConnectorsController {

    private static final Logger log = LoggerFactory.getLogger(ConnectorsController.class);
    private final ConnectorService connectorService;
    private final ReservationService reservationService;
    private final JwtUtil jwtUtil;

    public ConnectorsController(ConnectorService connectorService, ReservationService reservationService, JwtUtil jwtUtil) {
        this.connectorService = connectorService;
        this.reservationService = reservationService;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping("/add")
    public ResponseEntity<ResponseTemplate<Connector>> addConnector(@RequestBody @Valid AddRequest request){

        Connector added = connectorService.addConnector(request);

        return new ResponseEntity<>(new ResponseTemplate<>
                ("Connector added successfully", added), HttpStatus.OK);
    }

    @PutMapping("/reserve")
    public ResponseEntity<ResponseTemplate<ReservationResponse>> reserveConnector(@RequestHeader("Authorization") String token, @RequestBody @Valid ReserveRequest request){

        token = token.substring(7);

        String username = jwtUtil.extractUsername(token);
        Connector reserved = connectorService.reserveConnector(request);
        Reservation reservation = reservationService.createReservation(username, request);

        ReservationResponse reservationResponse = new ReservationResponse(username, reserved.getConnectorId(), reserved.getStation().getStationId(), reservation.getTimestamp(), reservation.getExpirationTime());

        return new ResponseEntity<>(new ResponseTemplate<>
                ("Connector reserved successfully", reservationResponse), HttpStatus.OK);
    }

}
