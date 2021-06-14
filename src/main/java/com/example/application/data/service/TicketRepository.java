package com.example.application.data.service;

import com.example.application.data.entity.Ticket;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.time.LocalDate;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

}