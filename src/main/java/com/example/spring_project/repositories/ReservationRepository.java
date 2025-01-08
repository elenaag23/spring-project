package com.example.spring_project.repositories;

import com.example.spring_project.models.entities.Connector;
import com.example.spring_project.models.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    @Query("SELECT r FROM Reservation r WHERE r.connectorId = :connectorId " +
            "and r.stationId = :stationId " +
            "and r.user.userId = :userId " +
            "and r.expirationTime > :now")
    Optional<Reservation> findByConnectorIdAndStationIdAndUserId(int stationId, int connectorId, int userId, LocalDateTime now);
}
