package com.example.demo.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private UserSessionStatus status;

    private Instant createdAt;
    private Instant expiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public UserSessionStatus getStatus() { return status; }
    public void setStatus(UserSessionStatus status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}