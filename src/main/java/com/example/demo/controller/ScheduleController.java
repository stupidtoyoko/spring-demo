package com.example.demo.controller;

import com.example.demo.model.Schedule;
import com.example.demo.repository.ScheduleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleRepository repo;

    public ScheduleController(ScheduleRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Schedule create(@RequestBody Schedule schedule) {
        return repo.save(schedule);
    }

    @GetMapping
    public List<Schedule> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Schedule getById(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Schedule update(@PathVariable Long id, @RequestBody Schedule updated) {
        return repo.findById(id).map(schedule -> {
            schedule.setStartTime(updated.getStartTime());
            schedule.setEndTime(updated.getEndTime());
            schedule.setAttraction(updated.getAttraction());
            return repo.save(schedule);
        }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}