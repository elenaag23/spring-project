package com.example.spring_project.controllers.stations;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class CoordinatesDto {
    private double latitude;
    private double longitude;

    public CoordinatesDto(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
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

    @Override
    public String toString() {
        return "CoordinatesDto{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
