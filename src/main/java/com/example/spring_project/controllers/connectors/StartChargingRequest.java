package com.example.spring_project.controllers.connectors;

public class StartChargingRequest {
    private int connectorId;

    private int stationId;

    private double revenue;

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
