package com.meditourism.meditourism.report.dto;

import com.meditourism.meditourism.comment.dto.CommentDTO;
import com.meditourism.meditourism.communityRule.dto.CommunityRuleResponseDTO;
import com.meditourism.meditourism.review.dto.ReviewResponseDTO;
import com.meditourism.meditourism.review.entity.ReviewEntity;
import com.meditourism.meditourism.user.dto.UserResponseDTO;
import com.meditourism.meditourism.report.entity.ReportEntity;

import java.util.ArrayList;
import java.util.List;

public class ReportResponseDTO {

    private Long id;
    private UserResponseDTO reporterUser;
    private ReviewResponseDTO targetReview;
    private CommentDTO targetComment;
    private CommunityRuleResponseDTO communityRule;
    private String reason;

    public ReportResponseDTO(Long id, UserResponseDTO reporterUser, ReviewResponseDTO targetReview,
                             CommentDTO targetComment, CommunityRuleResponseDTO communityRule, String reason) {
        this.id = id;
        this.reporterUser = reporterUser;
        this.targetReview = targetReview;
        this.targetComment = targetComment;
        this.communityRule = communityRule;
        this.reason = reason;
    }

    public ReportResponseDTO() {}

    public ReportResponseDTO(ReportEntity entity) {
        this.id = entity.getId();
        this.reporterUser = new UserResponseDTO(entity.getReporterUser());
        this.targetReview = entity.getTargetReview() != null ? new ReviewResponseDTO(entity.getTargetReview()) : null;
        this.targetComment = entity.getTargetComment() != null ? new CommentDTO(entity.getTargetComment()) : null;
        this.communityRule = new CommunityRuleResponseDTO(entity.getCommunityRule());
        this.reason = entity.getReason();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setReporterUser(UserResponseDTO reporterUser) {
        this.reporterUser = reporterUser;
    }

    public void setTargetReview(ReviewResponseDTO targetReview) {
        this.targetReview = targetReview;
    }

    public void setTargetComment(CommentDTO targetComment) {
        this.targetComment = targetComment;
    }

    public void setCommunityRule(CommunityRuleResponseDTO communityRule) {
        this.communityRule = communityRule;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getId() {
        return id;
    }

    public UserResponseDTO getReporterUser() {
        return reporterUser;
    }

    public ReviewResponseDTO getTargetReview() {
        return targetReview;
    }

    public CommentDTO getTargetComment() {
        return targetComment;
    }

    public CommunityRuleResponseDTO getCommunityRule() {
        return communityRule;
    }

    public String getReason() {
        return reason;
    }

    public static List<ReportResponseDTO> fromEntityList(List<ReportEntity> entities) {
        List<ReportResponseDTO> dtoList = new ArrayList<>();
        for (ReportEntity entity : entities) {
            dtoList.add(new ReportResponseDTO(entity));
        }
        return dtoList;
    }
}
