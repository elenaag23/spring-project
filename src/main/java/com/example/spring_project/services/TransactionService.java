package com.example.spring_project.services;

import com.example.spring_project.controllers.connectors.StartChargingRequest;
import com.example.spring_project.controllers.connectors.StopRequest;
import com.example.spring_project.enums.ConnectorStatus;
import com.example.spring_project.models.entities.Connector;
import com.example.spring_project.models.entities.Transaction;
import com.example.spring_project.models.entities.User;
import com.example.spring_project.repositories.ConnectorRepository;
import com.example.spring_project.repositories.TransactionRepository;
import com.example.spring_project.repositories.UserRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final ConnectorRepository connectorRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository, ConnectorRepository connectorRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.connectorRepository = connectorRepository;
    }

    public Transaction createTransaction(String username, @Valid StartChargingRequest request) {
        User user = userRepository.findByEmail(username).orElseThrow(() ->
                new IllegalArgumentException("User not found"));
        Connector connector = connectorRepository.findByConnectorIdAndStationId(request.getStationId(), request.getConnectorId()).orElseThrow(() ->
                new IllegalArgumentException("Connector not found"));
        Double amount = request.getRevenue() / connector.getPricePerKw();

        Transaction transaction = new Transaction(LocalDateTime.now(), connector, request.getRevenue(), amount, user);
        return transactionRepository.save(transaction);
    }

    public Transaction stopTransaction(@Valid StopRequest request) {
        log.info("entered stop transactions");
        Transaction transaction = transactionRepository.findByTransactionId(request.getTransactionId()).orElseThrow(() ->
                new IllegalArgumentException("Transaction not found"));

        transaction.setStopTime(LocalDateTime.now());
        Connector connector = connectorRepository.findByConnectorIdAndStationId(transaction.getConnector().getStation().getStationId(), transaction.getConnector().getConnectorId()).orElseThrow(() ->
                new IllegalArgumentException("Connector not found"));
        connector.setChargingStatus(ConnectorStatus.FREE.toString());
        connectorRepository.save(connector);

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getUserTransactions(String username) {
        User user = userRepository.findByEmail(username).orElseThrow();

        return user.getTransactions();
    }
}
