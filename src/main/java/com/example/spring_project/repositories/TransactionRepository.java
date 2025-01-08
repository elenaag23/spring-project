package com.example.spring_project.repositories;

import com.example.spring_project.models.entities.Transaction;
import com.example.spring_project.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Optional<Transaction> findByTransactionId(int transactionId);
}
