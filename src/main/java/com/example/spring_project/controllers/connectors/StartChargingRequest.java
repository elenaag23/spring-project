package com.example.spring_project.controllers.connectors;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public class StartChargingRequest {
    @NotEmpty(message = "You must provide the connector id")
    private int connectorId;

    @NotEmpty(message = "You must provide the station id")
    private int stationId;

    @NotEmpty(message = "You must provide the amount to charge in RON")
    @Min(value = 25, message = "You must charge for at least 25 RON")
    private double revenue;

    public StartChargingRequest() {
    }

    public StartChargingRequest(int connectorId, int stationId, double revenue) {
        this.connectorId = connectorId;
        this.stationId = stationId;
        this.revenue = revenue;
    }

    public int getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(int connectorId) {
        this.connectorId = connectorId;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }
}
