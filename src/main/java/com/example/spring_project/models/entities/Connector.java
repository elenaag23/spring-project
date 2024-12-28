package com.example.spring_project.models.entities;

import jakarta.persistence.*;

@Table
@Entity
public class Connector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int connectorId;

    private String currentType;

    private String connectorType;

    private Double maxPower;

    private Double pricePerKw;

    private String chargingStatus;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    public Connector() {
    }

    public Connector(int connectorId, String currentType, String connectorType, Double maxPower, Double pricePerKw, String chargingStatus, Station station) {
        this.connectorId = connectorId;
        this.currentType = currentType;
        this.connectorType = connectorType;
        this.maxPower = maxPower;
        this.pricePerKw = pricePerKw;
        this.chargingStatus = chargingStatus;
        this.station = station;
    }

    public int getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(int connectorId) {
        this.connectorId = connectorId;
    }

    public String getCurrentType() {
        return currentType;
    }

    public void setCurrentType(String currentType) {
        this.currentType = currentType;
    }

    public String getConnectorType() {
        return connectorType;
    }

    public void setConnectorType(String connectorType) {
        this.connectorType = connectorType;
    }

    public Double getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(Double maxPower) {
        this.maxPower = maxPower;
    }

    public Double getPricePerKw() {
        return pricePerKw;
    }

    public void setPricePerKw(Double pricePerKw) {
        this.pricePerKw = pricePerKw;
    }

    public String getChargingStatus() {
        return chargingStatus;
    }

    public void setChargingStatus(String chargingStatus) {
        this.chargingStatus = chargingStatus;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }


}
