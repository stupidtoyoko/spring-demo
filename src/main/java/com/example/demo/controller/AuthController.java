package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.security.JwtTokenProvider;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UserService userService,
                          AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // ------------------- Регистрация -------------------
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (password == null || password.length() < 6 || !password.matches(".*\\W.*")) {
            return ResponseEntity.badRequest()
                    .body("Пароль должен быть >= 6 символов и содержать спецсимвол");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER"); // роль по умолчанию
        user.setRoles(roles);

        User savedUser = userService.register(user);

        return ResponseEntity.ok("Пользователь зарегистрирован: " + savedUser.getUsername());
    }

    // ------------------- Логин -------------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        try {
            // Аутентификация
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // Получаем пользователя из БД
            User user = userService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

            // Преобразуем роли в Set<String>
            Set<String> rolesSet = new HashSet<>(user.getRoles());

            // Генерация токенов
            String accessToken = jwtTokenProvider.createAccessToken(username, rolesSet);
            String refreshToken = jwtTokenProvider.createRefreshToken(username);

            return ResponseEntity.ok(Map.of(
                    "accessToken", accessToken,
                    "refreshToken", refreshToken
            ));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Неверный логин или пароль");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка сервера: " + e.getMessage());
        }
    }

    // ------------------- Refresh -------------------
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");

        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Неверный или просроченный refresh токен");
        }

        String username = jwtTokenProvider.getUsername(refreshToken);

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Set<String> rolesSet = new HashSet<>(user.getRoles());

        String newAccessToken = jwtTokenProvider.createAccessToken(username, rolesSet);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(username);

        return ResponseEntity.ok(Map.of(
                "accessToken", newAccessToken,
                "refreshToken", newRefreshToken
        ));
    }
}