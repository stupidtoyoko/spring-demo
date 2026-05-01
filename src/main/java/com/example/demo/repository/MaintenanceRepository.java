package com.example.demo.repository;

import com.example.demo.model.Attraction;
import com.example.demo.model.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    List<Maintenance> findByAttraction(Attraction attraction);
}