package com.example.spring_project.services;

import com.example.spring_project.controllers.connectors.ReserveRequest;
import com.example.spring_project.models.entities.Reservation;
import com.example.spring_project.models.entities.User;
import com.example.spring_project.repositories.ReservationRepository;
import com.example.spring_project.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private static final int expirationMinutes = 15;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    }

    public Reservation createReservation(String username, ReserveRequest reserveRequest){
        User user = userRepository.findByEmail(username).orElseThrow();
        Reservation reservation = new Reservation(LocalDateTime.now(), LocalDateTime.now().plusMinutes(expirationMinutes), user, reserveRequest.getStationId(), reserveRequest.getConnectorId());
        return reservationRepository.save(reservation);
    }
}
