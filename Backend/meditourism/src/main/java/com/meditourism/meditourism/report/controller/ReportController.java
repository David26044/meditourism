package com.meditourism.meditourism.report.controller;

import com.meditourism.meditourism.report.dto.ReportRequestDTO;
import com.meditourism.meditourism.report.dto.ReportResponseDTO;
import com.meditourism.meditourism.report.service.IReportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private IReportService reportService;


    @GetMapping
    public ResponseEntity<List<ReportResponseDTO>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    // Obtener reporte por ID
    @GetMapping("/{id}")
    public ResponseEntity<ReportResponseDTO> getReportById(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.getReportById(id));
    }

    // Crear nuevo reporte
    @PostMapping
    public ResponseEntity<ReportResponseDTO> createReport(@RequestBody @Valid ReportRequestDTO dto) {
        ReportResponseDTO savedReport = reportService.saveReport(dto);
        return ResponseEntity.created(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(savedReport.getId())
                        .toUri())
                .body(savedReport);
    }

    // Actualizar reporte existente
    @PatchMapping("/{id}")
    public ResponseEntity<ReportResponseDTO> updateReport(@PathVariable Long id,
                                                          @RequestBody ReportRequestDTO dto) {
        return ResponseEntity.ok(reportService.updateReport(id, dto));
    }

    // Eliminar reporte
    @DeleteMapping("/{id}")
    public ResponseEntity<ReportResponseDTO> deleteReport(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.deleteReport(id));
    }
}
