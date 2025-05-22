package com.meditourism.meditourism.review.controller;

import com.meditourism.meditourism.review.dto.ReviewDTO;
import com.meditourism.meditourism.review.entity.ReviewEntity;
import com.meditourism.meditourism.review.service.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    IReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<ReviewEntity>> getAllReviews(){
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping("/review-clinic/{id}")
    public ResponseEntity<List<ReviewEntity>> getReviewsByClinicId(@PathVariable Long id){
        return ResponseEntity.ok(reviewService.getReviewsByClinicId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewEntity> getReviewByReviewId(@PathVariable Long id){
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @PostMapping
    public ResponseEntity<ReviewEntity> postReview(@RequestBody ReviewDTO dto){
        ReviewEntity savedReview = reviewService.saveReview(dto);
        return ResponseEntity
                .created(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("{/id}")
                        .buildAndExpand(savedReview.getId())
                        .toUri())
                .body(savedReview);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReviewEntity> patchReview(@PathVariable Long id, @RequestBody ReviewDTO dto){
        return ResponseEntity.ok(reviewService.updateReview(id, dto));
    }

    @DeleteMapping
    public ResponseEntity<ReviewEntity> deleteReview(@PathVariable Long id){
        return ResponseEntity.ok(reviewService.deleteReview(id));
    }

}
