package com.example.spring_project.repositories;

import com.example.spring_project.models.entities.Connector;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectorRepository extends JpaRepository<Connector, Integer> {
}