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

    private double latitude;

    private double longitude;

    @OneToMany(mappedBy = "station", cascade = CascadeType.ALL)
    private List<Connector> connectors;

    public Station() {
    }

    public Station(String stationName, String address) {
        this.stationName = stationName;
        this.address = address;
    }

    public Station(String stationName, String status, String address) {
        this.stationName = stationName;
        this.status = status;
        this.address = address;
    }

    public Station(int stationId, String stationName, String status, String address) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.status = status;
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
