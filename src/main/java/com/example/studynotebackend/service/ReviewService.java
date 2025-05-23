package com.example.studynotebackend.service;

import com.example.studynotebackend.domain.Review;
import com.example.studynotebackend.mapper.ReviewMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewMapper reviewMapper;

    public ReviewService(ReviewMapper reviewMapper) {
        this.reviewMapper = reviewMapper;
    }

    public List<Review> getReviewsByNoteId(Long noteId) {
        return reviewMapper.findByNoteId(noteId);
    }

    @Transactional
    public Review addReview(Review review) {
        reviewMapper.insert(review);
        return review;
    }
}
