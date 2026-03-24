package com.example.demo.controller;

import com.example.demo.model.Ticket;
import com.example.demo.repository.TicketRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketRepository repo;

    public TicketController(TicketRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Ticket create(@RequestBody Ticket ticket) {
        return repo.save(ticket);
    }

    @GetMapping
    public List<Ticket> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Ticket getById(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Ticket update(@PathVariable Long id, @RequestBody Ticket updated) {
        return repo.findById(id).map(ticket -> {
            ticket.setVisitor(updated.getVisitor());
            ticket.setSchedule(updated.getSchedule());
            return repo.save(ticket);
        }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}