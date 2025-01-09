package com.example.spring_project.controllers.users;

public class ProfileResponse {

    private String name;

    private String email;

    private int fidelityPoints;

    public ProfileResponse(String name, String email, int fidelityPoints) {
        this.name = name;
        this.email = email;
        this.fidelityPoints = fidelityPoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getFidelityPoints() {
        return fidelityPoints;
    }

    public void setFidelityPoints(int fidelityPoints) {
        this.fidelityPoints = fidelityPoints;
    }
}
