package com.meditourism.meditourism.report.service;

import com.meditourism.meditourism.report.dto.ReportRequestDTO;
import com.meditourism.meditourism.report.dto.ReportResponseDTO;

import java.util.List;

public interface IReportService {

    List<ReportResponseDTO> getAllReports();
    ReportResponseDTO getReportById(Long id);
    ReportResponseDTO saveReport(ReportRequestDTO dto);
    ReportResponseDTO updateReport(Long id, ReportRequestDTO dto);
    ReportResponseDTO deleteReport(Long id);

}
