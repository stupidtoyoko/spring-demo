package com.example.demo.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users") // таблица будет называться users
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    // ------------------- Роли -------------------
    // Отдельная таблица user_roles будет создана автоматически
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_roles",             // таблица для ролей
            joinColumns = @JoinColumn(name = "user_id") // внешний ключ на users.id
    )
    @Column(name = "role")              // имя колонки, где хранится роль
    private Set<String> roles = new HashSet<>();

    // ------------------- Геттеры и сеттеры -------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    // ------------------- Удобные методы -------------------
    public void addRole(String role) {
        this.roles.add(role);
    }

    public void removeRole(String role) {
        this.roles.remove(role);
    }
}