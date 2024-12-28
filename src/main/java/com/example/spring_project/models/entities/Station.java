package com.example.spring_project.models.entities;

import jakarta.persistence.*;

import java.util.List;

@Table
@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int stationId;

    private String stationName;

    private String status;

    private String address;

    @OneToMany(mappedBy = "station", cascade = CascadeType.ALL)
    private List<Connector> connectors;

    public Station() {
    }

    public Station(int stationId, String stationName, String status, String address) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.status = status;
        this.address = address;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Connector> getConnectors() {
        return connectors;
    }

    public void setConnectors(List<Connector> connectors) {
        this.connectors = connectors;
    }
}
