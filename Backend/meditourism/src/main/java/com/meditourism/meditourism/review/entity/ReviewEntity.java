package com.meditourism.meditourism.review.entity;

import com.meditourism.meditourism.clinic.entity.ClinicEntity;
import com.meditourism.meditourism.comment.entity.CommentEntity;
import com.meditourism.meditourism.user.entity.UserEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "reviews")
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "clinic_id", nullable = false)
    private ClinicEntity clinicEntity;

    @Column(nullable = false) // contenido no nulo
    private String content;

    @Column
    @CreationTimestamp
    private LocalDateTime date;

    @Transient
    private List<CommentEntity> comments = new ArrayList<>();
    // Getters y Setters

    public Long getId() {
        return id;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public void addComment(CommentEntity comment) {
        comments.add(comment);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getPublicationDate() {
        return date;
    }

    public void setPublicationDate(LocalDateTime publicationDate) {
        this.date = date;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
