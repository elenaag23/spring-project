package com.example.spring_project.services;

import com.example.spring_project.controllers.connectors.AddRequest;
import com.example.spring_project.controllers.connectors.ReserveRequest;
import com.example.spring_project.controllers.connectors.StartChargingRequest;
import com.example.spring_project.enums.ConnectorStatus;
import com.example.spring_project.models.entities.Connector;
import com.example.spring_project.models.entities.Reservation;
import com.example.spring_project.models.entities.Station;
import com.example.spring_project.models.entities.User;
import com.example.spring_project.repositories.ConnectorRepository;
import com.example.spring_project.repositories.ReservationRepository;
import com.example.spring_project.repositories.StationRepository;
import com.example.spring_project.repositories.UserRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ConnectorService {

    private static final Logger log = LoggerFactory.getLogger(ConnectorService.class);
    private final ConnectorRepository connectorRepository;
    private final StationRepository stationRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    public ConnectorService(ConnectorRepository connectorRepository, StationRepository stationRepository, UserRepository userRepository, ReservationRepository reservationRepository) {
        this.connectorRepository = connectorRepository;
        this.stationRepository = stationRepository;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
    }

    public Connector addConnector(@Valid AddRequest request) {
        try{
            Station station = stationRepository.findById(request.getStationId()).orElseThrow();
            Connector connector = new Connector(request.getCurrentType().toString(), request.getConnectorType().toString(), request.getMaxPower(), request.getPrice(), request.getStatus().toString(), station);
            return connectorRepository.save(connector);
        } catch (Exception e){
            throw new IllegalArgumentException("Incorrect connector data");
        }
    }

    public Connector reserveConnector(@Valid ReserveRequest request) {
        log.info("entered reserve: " + request);
        Connector connector = connectorRepository.findByConnectorIdAndStationId(request.getStationId(), request.getConnectorId()).orElseThrow();

        log.info("selected connector: " + connector);

        if(!connector.getChargingStatus().equals(ConnectorStatus.FREE.toString())){
            throw new IllegalArgumentException("Connector unavailable");
        }

        connector.setChargingStatus(ConnectorStatus.RESERVED.toString());
        return connectorRepository.save(connector);
    }

    public Connector startCharging(String username, @Valid StartChargingRequest request) {
        User user = userRepository.findByEmail(username).orElseThrow();
        Connector connector = connectorRepository.findByConnectorIdAndStationId(request.getStationId(), request.getConnectorId()).orElseThrow();

        if(connector.getChargingStatus().equals(ConnectorStatus.RESERVED.toString())){
            Reservation availableReservation = reservationRepository.findByConnectorIdAndStationIdAndUserId
                    (request.getStationId(), request.getConnectorId(), user.getUserId(),
                            LocalDateTime.now()).orElseThrow(() ->
                            new IllegalArgumentException("User has not reserved this station"));

            if(availableReservation != null){
                connector.setChargingStatus(ConnectorStatus.CHARGING.toString());
                return connectorRepository.save(connector);
            }

        } else if (connector.getChargingStatus().equals(ConnectorStatus.FREE.toString())){
            connector.setChargingStatus(ConnectorStatus.CHARGING.toString());
            return connectorRepository.save(connector);
        }

        return  null;

    }
}
