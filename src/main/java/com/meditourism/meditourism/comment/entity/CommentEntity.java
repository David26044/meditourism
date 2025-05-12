package com.meditourism.meditourism.comment.entity;

import com.meditourism.meditourism.review.entity.ReviewEntity;
import com.meditourism.meditourism.user.entity.UserEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "comments")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Column
    @CreationTimestamp
    private LocalDateTime date = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private ReviewEntity reviewEntity;

    @ManyToOne
    @JoinColumn(name = "father_comment_id")
    private CommentEntity fatherCommentEntity;

}
