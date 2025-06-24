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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reporter_user", nullable = false)
    private UserEntity reporterUser;

    @ManyToOne
    @JoinColumn(name = "target_review_id")
    private ReviewEntity targetReview;

    @ManyToOne
    @JoinColumn(name = "target_comment_id")
    private CommentEntity targetComment;

    @ManyToOne
    @JoinColumn(name = "community_rule_id", nullable = false)
    private CommunityRuleEntity communityRule;

    @Column(nullable = false)
    private String reason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getReporterUser() {
        return reporterUser;
    }

    public void setReporterUser(UserEntity reporterUser) {
        this.reporterUser = reporterUser;
    }

    public ReviewEntity getTargetReview() {
        return targetReview;
    }

    public void setTargetReview(ReviewEntity targetReview) {
        this.targetReview = targetReview;
    }

    public CommentEntity getTargetComment() {
        return targetComment;
    }

    public void setTargetComment(CommentEntity targetComment) {
        this.targetComment = targetComment;
    }

    public CommunityRuleEntity getCommunityRule() {
        return communityRule;
    }

    public void setCommunityRule(CommunityRuleEntity communityRule) {
        this.communityRule = communityRule;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
