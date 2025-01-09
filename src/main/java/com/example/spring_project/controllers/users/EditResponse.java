package com.example.spring_project.controllers.users;

public class EditResponse {
    private String email;

    private String name;

    public EditResponse() {
    }

    public EditResponse(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
