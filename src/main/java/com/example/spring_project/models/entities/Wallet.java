package com.example.spring_project.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

@Table
@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int walletId;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    private int fidelityPoints;

    public Wallet() {
    }

    public Wallet(int walletId, User user, int fidelityPoints) {
        this.walletId = walletId;
        this.user = user;
        this.fidelityPoints = fidelityPoints;
    }

    public Wallet(User user, int fidelityPoints) {
        this.user = user;
        this.fidelityPoints = fidelityPoints;
    }

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getFidelityPoints() {
        return fidelityPoints;
    }

    public void setFidelityPoints(int fidelityPoints) {
        this.fidelityPoints = fidelityPoints;
    }
}
