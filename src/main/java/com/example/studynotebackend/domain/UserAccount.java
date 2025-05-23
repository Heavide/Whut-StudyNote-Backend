package com.example.studynotebackend.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserAccount {
    private Long id;
    private String username;
    private String email;
    private String passwordHash;
    private LocalDateTime createdAt;
}
