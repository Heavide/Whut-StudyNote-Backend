package com.example.studynotebackend.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Note {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
