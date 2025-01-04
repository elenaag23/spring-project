package com.example.spring_project.controllers.connectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReserveRequest {

    private int stationId;

    private int connectorId;

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

    @Override
    public String toString() {
        return "ReserveRequest{" +
                "stationId=" + stationId +
                ", connectorId=" + connectorId +
                '}';
    }
}
