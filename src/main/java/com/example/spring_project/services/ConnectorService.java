package com.example.spring_project.services;

import com.example.spring_project.controllers.connectors.AddRequest;
import com.example.spring_project.models.entities.Connector;
import com.example.spring_project.models.entities.Station;
import com.example.spring_project.repositories.ConnectorRepository;
import com.example.spring_project.repositories.StationRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class ConnectorService {

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
}
