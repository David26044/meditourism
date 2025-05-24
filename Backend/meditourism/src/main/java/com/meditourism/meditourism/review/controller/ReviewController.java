package com.meditourism.meditourism.review.controller;

import com.meditourism.meditourism.review.dto.ReviewDTO;
import com.meditourism.meditourism.review.entity.ReviewEntity;
import com.meditourism.meditourism.review.service.IReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    IReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews(){
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping("/review-clinic/{id}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByClinicId(@PathVariable Long id){
        return ResponseEntity.ok(reviewService.getReviewsByClinicId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewByReviewId(@PathVariable Long id){
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> postReview(@RequestBody @Valid ReviewDTO dto){
        ReviewDTO savedReview = reviewService.saveReview(dto);
        return ResponseEntity
                .created(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("{/id}")
                        .buildAndExpand(savedReview.getId())
                        .toUri())
                .body(savedReview);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReviewDTO> patchReview(@PathVariable Long id, @RequestBody ReviewDTO dto, Authentication authenticate){
        return ResponseEntity.ok(reviewService.updateReview(id, dto, authenticate));
    }

    @DeleteMapping
    public ResponseEntity<ReviewDTO> deleteReview(@PathVariable Long id, Authentication authenticate){
        return ResponseEntity.ok(reviewService.deleteReview(id, authenticate));
    }

}
