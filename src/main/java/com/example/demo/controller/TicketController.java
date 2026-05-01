package com.example.demo.controller;

import com.example.demo.model.Ticket;
import com.example.demo.repository.TicketRepository;
import com.example.demo.service.TicketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketRepository repo;
    private final TicketService service;

    public TicketController(TicketRepository repo, TicketService service) {
        this.repo = repo;
        this.service = service;
    }

    @PostMapping("/buy")
    public Ticket buy(@RequestBody Ticket ticket) {
        return service.buyTicket(ticket);
    }

    @GetMapping
    public List<Ticket> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Ticket getById(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}