package com.example.spring_project.controllers.connectors;

import jakarta.validation.constraints.NotEmpty;
import lombok.NonNull;

public class StopRequest {
    @NotEmpty(message = "You must provide the transaction id to stop")
    private int transactionId;

    public StopRequest() {
    }

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
