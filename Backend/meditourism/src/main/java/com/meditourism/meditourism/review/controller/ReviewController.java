package com.meditourism.meditourism.review.controller;

import com.meditourism.meditourism.review.dto.ReviewRequestDTO;
import com.meditourism.meditourism.review.dto.ReviewResponseDTO;
import com.meditourism.meditourism.review.service.IReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<ReviewResponseDTO>> getAllReviews(){
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping("/review-clinic/{id}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByClinicId(@PathVariable Long id){
        return ResponseEntity.ok(reviewService.getReviewsByClinicId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> getReviewByReviewId(@PathVariable Long id){
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDTO> postReview(@RequestBody @Valid ReviewRequestDTO dto){
        ReviewResponseDTO savedReview = reviewService.saveReview(dto);
        return ResponseEntity
                .created(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("{/id}")
                        .buildAndExpand(savedReview.getId())
                        .toUri())
                .body(savedReview);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> patchReview(@PathVariable Long id, @RequestBody ReviewRequestDTO dto){
        return ResponseEntity.ok(reviewService.updateReview(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> deleteReview(@PathVariable Long id){
        return ResponseEntity.ok(reviewService.deleteReview(id));
    }

    @GetMapping("/latest")
    public ResponseEntity<List<ReviewResponseDTO>> getLatestThreeReviews() {
        return ResponseEntity.ok(reviewService.getLatestThreeReviews());
    }

}
