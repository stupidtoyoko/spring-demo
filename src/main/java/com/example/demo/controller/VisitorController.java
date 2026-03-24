package com.example.demo.controller;

import com.example.demo.model.Visitor;
import com.example.demo.repository.VisitorRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visitors")
public class VisitorController {

    private final VisitorRepository repo;

    public VisitorController(VisitorRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Visitor create(@RequestBody Visitor visitor) {
        return repo.save(visitor);
    }

    @GetMapping
    public List<Visitor> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Visitor getById(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Visitor update(@PathVariable Long id, @RequestBody Visitor updated) {
        return repo.findById(id).map(visitor -> {
            visitor.setName(updated.getName());
            visitor.setEmail(updated.getEmail());
            visitor.setPhone(updated.getPhone());
            return repo.save(visitor);
        }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}