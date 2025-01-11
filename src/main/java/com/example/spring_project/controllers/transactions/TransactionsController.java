package com.example.spring_project.controllers.transactions;

import com.example.spring_project.models.entities.Transaction;
import com.example.spring_project.security.JwtUtil;
import com.example.spring_project.services.TransactionService;
import com.example.spring_project.utils.ResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction")
@Tag(name = "Transactions", description = "Endpoints for managing user transactions")
public class TransactionsController {

    private final TransactionService transactionService;
    private final JwtUtil jwtUtil;

    public TransactionsController(TransactionService transactionService, JwtUtil jwtUtil) {
        this.transactionService = transactionService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "Get user transactions", description = "Retrieves a list of transactions for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Transaction.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/get")
    public ResponseEntity<ResponseTemplate<List<Transaction>>> getUserTransactions(
            @Parameter(description = "Authorization token")
            @RequestHeader(value = "Authorization") String token) {

        token = token.substring(7);
        String username = jwtUtil.extractUsername(token);

        List<Transaction> userTransactions = transactionService.getUserTransactions(username);

        return new ResponseEntity<>(new ResponseTemplate<>("Transactions retrieved successfully", userTransactions), HttpStatus.OK);
    }
}
