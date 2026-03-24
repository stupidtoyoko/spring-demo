package com.example.demo.controller;

import com.example.demo.model.Maintenance;
import com.example.demo.repository.MaintenanceRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {

    private final MaintenanceRepository repo;

    public MaintenanceController(MaintenanceRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Maintenance create(@RequestBody Maintenance maintenance) {
        return repo.save(maintenance);
    }

    @GetMapping
    public List<Maintenance> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Maintenance getById(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Maintenance update(@PathVariable Long id, @RequestBody Maintenance updated) {
        return repo.findById(id).map(maintenance -> {
            maintenance.setStartTime(updated.getStartTime());
            maintenance.setEndTime(updated.getEndTime());
            maintenance.setDescription(updated.getDescription());
            maintenance.setAttraction(updated.getAttraction());
            return repo.save(maintenance);
        }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}