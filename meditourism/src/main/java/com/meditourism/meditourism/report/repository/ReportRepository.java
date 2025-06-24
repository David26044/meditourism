package com.meditourism.meditourism.report.repository;

import com.meditourism.meditourism.report.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<ReportEntity, Long> {
}
