package com.example.application.data.service;

import com.example.application.data.entity.Ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import java.time.LocalDate;
import java.time.LocalDate;

@Service
public class TicketService extends CrudService<Ticket, Integer> {

    private TicketRepository repository;

    public TicketService(@Autowired TicketRepository repository) {
        this.repository = repository;
    }

    @Override
    protected TicketRepository getRepository() {
        return repository;
    }

}
