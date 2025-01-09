package com.example.spring_project.services;

import com.example.spring_project.models.entities.User;
import com.example.spring_project.models.entities.Wallet;
import com.example.spring_project.repositories.WalletRepository;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    private static final double coefficient = 0.1;


    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public void addPoints(User user, Double kwCharged){
        Wallet wallet = user.getWallet();

        int transactionsNumber = user.getTransactions().size();

        int amount = (int) (kwCharged + coefficient * transactionsNumber);

        wallet.setFidelityPoints(wallet.getFidelityPoints() + amount);
        walletRepository.save(wallet);

    }
}
