package com.meditourism.meditourism.report.service;

import com.meditourism.meditourism.auth.service.AuthService;
import com.meditourism.meditourism.comment.entity.CommentEntity;
import com.meditourism.meditourism.communityRule.entity.CommunityRuleEntity;
import com.meditourism.meditourism.exception.ResourceNotFoundException;
import com.meditourism.meditourism.report.dto.ReportRequestDTO;
import com.meditourism.meditourism.report.dto.ReportResponseDTO;
import com.meditourism.meditourism.report.entity.ReportEntity;
import com.meditourism.meditourism.report.repository.ReportRepository;
import com.meditourism.meditourism.review.entity.ReviewEntity;
import com.meditourism.meditourism.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService implements IReportService {

    @Autowired
    ReportRepository reportRepository;
    @Autowired
    private AuthService authService;

    /**
     * @return 
     */
    @Override
    public List<ReportResponseDTO> getAllReports() {
        return ReportResponseDTO.fromEntityList(reportRepository.findAll());
    }

    /**
     * @param id 
     * @return
     */
    @Override
    public ReportResponseDTO getReportById(Long id) {
        return new ReportResponseDTO(reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el reporte con ID: " + id)));
    }

    /**
     * @param dto 
     * @return
     */
    @Override
    public ReportResponseDTO saveReport(ReportRequestDTO dto) {
        ReportEntity entity = new ReportEntity();
        UserEntity user = authService.getAuthenticatedUser();
        CommunityRuleEntity communityRule = new CommunityRuleEntity();
        communityRule.setId(dto.getCommunityRuleId());
        if(dto.getTargetCommentId() != null) {
            CommentEntity comment = new CommentEntity();
            comment.setId(dto.getTargetCommentId());
            entity.setTargetComment(comment);
        }
        if (dto.getTargetReviewId() != null) {
            ReviewEntity review = new ReviewEntity();
            review.setId(dto.getTargetReviewId());
            entity.setTargetReview(review);
        }
        entity.setReporterUser(user);
        entity.setCommunityRule(communityRule);
        entity.setReason(dto.getReason());

        return new ReportResponseDTO(reportRepository.save(entity));
    }

    /**
     * @param dto 
     * @return
     */
    @Override
    public ReportResponseDTO updateReport(Long id, ReportRequestDTO dto) {
        ReportEntity entity = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el reporte con ID: "+ id));
        if (dto.getReason() != null) {
            entity.setReason(dto.getReason());
        }
        return new ReportResponseDTO(reportRepository.save(entity));
    }

    /**
     * @param id 
     * @return
     */
    @Override
    public ReportResponseDTO deleteReport(Long id) {
        ReportEntity entity = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el reporte con ID: "+ id));
        reportRepository.delete(entity);
        return new ReportResponseDTO(entity);
    }
}
