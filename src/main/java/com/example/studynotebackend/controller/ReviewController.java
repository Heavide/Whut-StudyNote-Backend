package com.example.studynotebackend.controller;

import com.example.studynotebackend.domain.Review;
import com.example.studynotebackend.dto.ReviewRequest;
import com.example.studynotebackend.service.ReviewService;
import com.example.studynotebackend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes/{noteId}/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final JwtUtil jwtUtils;

    public ReviewController(ReviewService reviewService, JwtUtil jwtUtils) {
        this.reviewService = reviewService;
        this.jwtUtils       = jwtUtils;
    }

    @GetMapping
    public ResponseEntity<List<Review>> list(@PathVariable Long noteId) {
        List<Review> reviews = reviewService.getReviewsByNoteId(noteId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping
    public ResponseEntity<Review> create(
            @PathVariable Long noteId,
            @RequestBody ReviewRequest req,
            HttpServletRequest request
    ) {
        // 提取 token
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }
        String token = authHeader.substring(7);

        // 拿 userId
        Long currentUserId = jwtUtils.getUserIdFromToken(token);

        Review review = new Review();
        review.setNoteId(noteId);
        review.setUserId(currentUserId);
        review.setRating(req.getRating());
        review.setComment(req.getComment());

        Review saved = reviewService.addReview(review);
        return ResponseEntity.ok(saved);
    }
}
