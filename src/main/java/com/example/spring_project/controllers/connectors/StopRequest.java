package com.example.spring_project.controllers.connectors;

public class StopRequest {
    private int transactionId;

    public StopRequest(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
}
