package com.meditourism.meditourism.schedule.repository;

import com.meditourism.meditourism.schedule.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {


    List<ScheduleEntity> findByClinicIdAndDayOfWeek(Long clinicId, Integer dayOfWeek);
    List<ScheduleEntity> findByClinicId(Long clinicId);
}
