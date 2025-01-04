package com.example.spring_project.repositories;

import com.example.spring_project.models.entities.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, Integer> {
    Optional<Station> findById(int stationId);
}
