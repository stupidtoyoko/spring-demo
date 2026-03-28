package com.example.demo.repository;

import com.example.demo.model.UserSession;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.UserSessionStatus;

import java.util.List;
import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    List<UserSession> findByUserAndStatus(User user, UserSessionStatus status);
    Optional<UserSession> findByRefreshToken(String refreshToken);
}