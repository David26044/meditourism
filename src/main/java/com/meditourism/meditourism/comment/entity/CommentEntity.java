package com.meditourism.meditourism.comment.entity;

import com.meditourism.meditourism.review.entity.ReviewEntity;
import com.meditourism.meditourism.user.entity.UserEntity;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "comments")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

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
