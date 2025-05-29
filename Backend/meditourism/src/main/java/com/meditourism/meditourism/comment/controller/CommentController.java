package com.meditourism.meditourism.comment.controller;

import com.meditourism.meditourism.comment.dto.CommentDTO;
import com.meditourism.meditourism.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // Obtener todos los comentarios
    @GetMapping
    public ResponseEntity<List<CommentDTO>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }

    // Obtener comentarios por ID de reseña
    @GetMapping("/review/{id}")
    public ResponseEntity<List<CommentDTO>> getCommentsByReviewId(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentsByReviewId(id));
    }

    // Obtener respuestas a un comentario
    @GetMapping("/reply/{id}")
    public ResponseEntity<List<CommentDTO>> getRepliesByCommentId(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getRepliesByCommentId(id));
    }

    // Obtener comentario por ID
    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<CommentDTO>> getCommentsByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentsByUserId(id));
    }

    // Crear nuevo comentario
    @PostMapping
    public ResponseEntity<CommentDTO> saveComment(@RequestBody CommentDTO dto) {
        CommentDTO savedComment = commentService.saveComment(dto);
        return ResponseEntity.created(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("{/id}")
                .buildAndExpand(savedComment.getId())
                .toUri())
                .body(savedComment);
    }

    // Actualizar un comentario
    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long id, @RequestBody CommentDTO dto) {
        return ResponseEntity.ok(commentService.updateComment(id, dto));
    }

    // Eliminar un comentario
    @DeleteMapping("/{id}")
    public ResponseEntity<CommentDTO> deleteComment(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.deleteComment(id));
    }
}
