package com.example.application.data.generator;

import com.vaadin.flow.spring.annotation.SpringComponent;

import com.example.application.data.service.TicketRepository;
import com.example.application.data.entity.Ticket;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.vaadin.artur.exampledata.DataType;
import org.vaadin.artur.exampledata.ExampleDataGenerator;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(TicketRepository ticketRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (ticketRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");

            logger.info("... generating 100 Ticket entities...");
            ExampleDataGenerator<Ticket> ticketRepositoryGenerator = new ExampleDataGenerator<>(Ticket.class,
                    LocalDateTime.of(2021, 6, 14, 0, 0, 0));
            ticketRepositoryGenerator.setData(Ticket::setId, DataType.ID);
            ticketRepositoryGenerator.setData(Ticket::setDescripcion, DataType.SENTENCE);
            ticketRepositoryGenerator.setData(Ticket::setEstado, DataType.WORD);
            ticketRepositoryGenerator.setData(Ticket::setCategoria, DataType.WORD);
            ticketRepositoryGenerator.setData(Ticket::setPrioridad, DataType.WORD);
            ticketRepositoryGenerator.setData(Ticket::setFechaCreacion, DataType.DATE_OF_BIRTH);
            ticketRepositoryGenerator.setData(Ticket::setFechaUltimoEstado, DataType.DATE_OF_BIRTH);
            ticketRepositoryGenerator.setData(Ticket::setMailCOntacto, DataType.EMAIL);
            ticketRepository.saveAll(ticketRepositoryGenerator.create(100, seed));

            logger.info("Generated demo data");
        };
    }

}