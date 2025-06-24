package com.meditourism.meditourism.report.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReportRequestDTO {


    private Long targetReviewId;   // puede ser null
    private Long targetCommentId;  // puede ser null

    @NotNull(message = "El id de la regla no puede ser nulo")
    private Long communityRuleId;

    @NotBlank(message = "La razon no puede estar vacía")
    private String reason;

    public Long getTargetReviewId() {
        return targetReviewId;
    }

    public void setTargetReviewId(Long targetReviewId) {
        this.targetReviewId = targetReviewId;
    }

    public Long getTargetCommentId() {
        return targetCommentId;
    }

    public void setTargetCommentId(Long targetCommentId) {
        this.targetCommentId = targetCommentId;
    }

    public Long getCommunityRuleId() {
        return communityRuleId;
    }

    public void setCommunityRuleId(Long communityRuleId) {
        this.communityRuleId = communityRuleId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
