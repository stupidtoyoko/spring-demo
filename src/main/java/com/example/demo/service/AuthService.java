package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.model.UserSession;
import com.example.demo.model.UserSessionStatus;
import com.example.demo.repository.UserSessionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AuthService {

    private final UserSessionRepository sessionRepository;

    public AuthService(UserSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    // Сохраняем refresh токен и инвалидируем старые
    public UserSession createSession(User user, String refreshToken, Instant expiresAt) {

        // Инвалидируем старые активные сессии
        List<UserSession> activeSessions = sessionRepository.findByUserAndStatus(user, UserSessionStatus.ACTIVE);
        for (UserSession s : activeSessions) {
            s.setStatus(UserSessionStatus.REVOKED);
        }
        sessionRepository.saveAll(activeSessions);

        // Создаём новую сессию
        UserSession newSession = new UserSession();
        newSession.setUser(user);
        newSession.setRefreshToken(refreshToken);
        newSession.setStatus(UserSessionStatus.ACTIVE);
        newSession.setCreatedAt(Instant.now());
        newSession.setExpiresAt(expiresAt);

        return sessionRepository.save(newSession);
    }

    // Проверка токена
    public UserSession validateRefreshToken(String refreshToken) {
        UserSession session = sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh токен не найден"));

        if (session.getStatus() != UserSessionStatus.ACTIVE || session.getExpiresAt().isBefore(Instant.now())) {
            session.setStatus(UserSessionStatus.EXPIRED);
            sessionRepository.save(session);
            throw new RuntimeException("Refresh токен невалиден");
        }
        return session;
    }
}