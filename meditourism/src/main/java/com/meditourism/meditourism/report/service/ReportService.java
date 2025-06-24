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

/**
 * Servicio para la gestión de reportes en el sistema.
 * Permite crear, consultar, actualizar y eliminar reportes de comentarios o reseñas
 * que violen las normas de la comunidad.
 */
@Service
public class ReportService implements IReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private AuthService authService;

    /**
     * Obtiene todos los reportes existentes en el sistema.
     *
     * @return Lista de todos los reportes en formato DTO
     */
    @Override
    public List<ReportResponseDTO> getAllReports() {
        return ReportResponseDTO.fromEntityList(reportRepository.findAll());
    }

    /**
     * Obtiene un reporte específico por su ID.
     *
     * @param id ID del reporte a buscar
     * @return DTO con los datos del reporte encontrado
     * @throws ResourceNotFoundException si no se encuentra el reporte
     */
    @Override
    public ReportResponseDTO getReportById(Long id) {
        return new ReportResponseDTO(reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el reporte con ID: " + id)));
    }

    /**
     * Crea un nuevo reporte en el sistema.
     *
     * @param dto DTO con los datos del reporte a crear
     * @return DTO con los datos del reporte creado
     */
    @Override
    public ReportResponseDTO saveReport(ReportRequestDTO dto) {
        ReportEntity entity = new ReportEntity();
        UserEntity user = authService.getAuthenticatedUser();
        CommunityRuleEntity communityRule = new CommunityRuleEntity();
        communityRule.setId(dto.getCommunityRuleId());

        // Establece el comentario reportado si existe
        if(dto.getTargetCommentId() != null) {
            CommentEntity comment = new CommentEntity();
            comment.setId(dto.getTargetCommentId());
            entity.setTargetComment(comment);
        }

        // Establece la reseña reportada si existe
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
     * Actualiza un reporte existente.
     *
     * @param id ID del reporte a actualizar
     * @param dto DTO con los nuevos datos del reporte
     * @return DTO con los datos del reporte actualizado
     * @throws ResourceNotFoundException si no se encuentra el reporte
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
     * Elimina un reporte del sistema.
     *
     * @param id ID del reporte a eliminar
     * @return DTO con los datos del reporte eliminado
     * @throws ResourceNotFoundException si no se encuentra el reporte
     */
    @Override
    public ReportResponseDTO deleteReport(Long id) {
        ReportEntity entity = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el reporte con ID: "+ id));
        reportRepository.delete(entity);
        return new ReportResponseDTO(entity);
    }
}