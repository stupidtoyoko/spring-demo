package com.example.demo.repository;

import com.example.demo.model.Attraction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttractionRepository extends JpaRepository<Attraction, Long> {}