package com.meditourism.meditourism.report.entity;

import com.meditourism.meditourism.comment.entity.CommentEntity;
import com.meditourism.meditourism.communityRule.entity.CommunityRuleEntity;
import com.meditourism.meditourism.review.entity.ReviewEntity;
import com.meditourism.meditourism.user.entity.UserEntity;
import jakarta.persistence.*;

@Entity
@Table(name="reports")
public class ReportEntity {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reporter_user", nullable = false)
    private UserEntity reporterUser;

    @ManyToOne
    @JoinColumn(name = "target_review_id", nullable = true)
    private ReviewEntity targetReview;

    @ManyToOne
    @JoinColumn(name = "target_comment_id", nullable = true)
    private CommentEntity targetComment;

    @ManyToOne
    @JoinColumn(name = "community_rule_id", nullable = false)
    private CommunityRuleEntity communityRule;

    @Column(nullable = false)
    private String reason;


}
