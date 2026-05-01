package com.example.demo.repository;

import com.example.demo.model.Schedule;
import com.example.demo.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    long countBySchedule(Schedule schedule);
}