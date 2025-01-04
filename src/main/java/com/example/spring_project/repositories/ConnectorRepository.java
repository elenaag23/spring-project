package com.example.spring_project.repositories;

import com.example.spring_project.models.entities.Connector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ConnectorRepository extends JpaRepository<Connector, Integer> {
    @Query("SELECT c FROM Connector c WHERE c.connectorId = :connectorId and c.station.stationId = :stationId")
    Optional<Connector> findByConnectorIdAndStationId(int stationId, int connectorId);
}
