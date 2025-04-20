package com.meditourism.meditourism.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "reviews")
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // contenido no nulo
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "publication_date", nullable = false)
    private Date publicationDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

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

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
