package com.example.studynotebackend.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Review {
    private Long id;
    private Long noteId;
    private Long userId;
    private Integer rating;        // 1–5 星
    private String comment;        // 可选
    private String username;
    private LocalDateTime createdAt;
}
