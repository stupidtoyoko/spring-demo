package com.example.demo.controller;

import com.example.demo.model.Attraction;
import com.example.demo.repository.AttractionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attractions")
public class AttractionController {

    private final AttractionRepository repo;

    public AttractionController(AttractionRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Attraction create(@RequestBody Attraction attraction) {
        return repo.save(attraction);
    }

    @GetMapping
    public List<Attraction> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Attraction getById(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Attraction update(@PathVariable Long id, @RequestBody Attraction updated) {
        return repo.findById(id).map(attraction -> {
            attraction.setName(updated.getName());
            attraction.setDescription(updated.getDescription());
            attraction.setCapacity(updated.getCapacity());
            return repo.save(attraction);
        }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}