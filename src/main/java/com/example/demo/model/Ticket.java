package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Visitor visitor;

    @ManyToOne
    private Schedule schedule;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Visitor getVisitor() { return visitor; }
    public void setVisitor(Visitor visitor) { this.visitor = visitor; }
    public Schedule getSchedule() { return schedule; }
    public void setSchedule(Schedule schedule) { this.schedule = schedule; }
}