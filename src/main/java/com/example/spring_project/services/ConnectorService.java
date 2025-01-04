package com.example.spring_project.services;

import com.example.spring_project.controllers.connectors.AddRequest;
import com.example.spring_project.controllers.connectors.ReserveRequest;
import com.example.spring_project.enums.ConnectorStatus;
import com.example.spring_project.models.entities.Connector;
import com.example.spring_project.models.entities.Station;
import com.example.spring_project.repositories.ConnectorRepository;
import com.example.spring_project.repositories.StationRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ConnectorService {

    private static final Logger log = LoggerFactory.getLogger(ConnectorService.class);
    private final ConnectorRepository connectorRepository;
    private final StationRepository stationRepository;

    public ConnectorService(ConnectorRepository connectorRepository, StationRepository stationRepository) {
        this.connectorRepository = connectorRepository;
        this.stationRepository = stationRepository;
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
}
