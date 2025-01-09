package com.example.spring_project.controllers.transactions;

import com.example.spring_project.controllers.connectors.AddRequest;
import com.example.spring_project.models.entities.Connector;
import com.example.spring_project.models.entities.Transaction;
import com.example.spring_project.security.JwtUtil;
import com.example.spring_project.services.TransactionService;
import com.example.spring_project.utils.ResponseTemplate;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionsController {

    private final TransactionService transactionService;
    private final JwtUtil jwtUtil;

    public TransactionsController(TransactionService transactionService, JwtUtil jwtUtil) {
        this.transactionService = transactionService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/get")
    public ResponseEntity<ResponseTemplate<List<Transaction>>> getUserTransactions(@RequestHeader(value = "Authorization") String token){

        token = token.substring(7);
        String username = jwtUtil.extractUsername(token);

        List<Transaction> getUserTransactions = transactionService.getUserTransactions(username);

        return new ResponseEntity<>(new ResponseTemplate<>
                ("Connector added successfully", getUserTransactions), HttpStatus.OK);
    }
}
