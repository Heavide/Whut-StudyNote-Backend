package com.example.studynotebackend.dto;

import lombok.Getter;

@Getter
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String createdAt;

    public UserDto() {}

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}