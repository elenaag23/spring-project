package com.example.spring_project.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Table
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reservationId;

    private LocalDateTime timestamp;

    private LocalDateTime expirationTime;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    private int stationId;

    private int connectorId;

    public Reservation(LocalDateTime timestamp, LocalDateTime expirationTime, User user) {
        this.timestamp = timestamp;
        this.expirationTime = expirationTime;
        this.user = user;
    }

    public Reservation(LocalDateTime timestamp, LocalDateTime expirationTime, User user, int stationId, int connectorId) {
        this.timestamp = timestamp;
        this.expirationTime = expirationTime;
        this.user = user;
        this.stationId = stationId;
        this.connectorId = connectorId;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public int getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(int connectorId) {
        this.connectorId = connectorId;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
