package com.meditourism.meditourism.schedule;

import com.meditourism.meditourism.clinic.entity.ClinicEntity;
import com.meditourism.meditourism.doctor.entity.DoctorEntity;
import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "schedules")
public class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private DoctorEntity doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", nullable = false)
    private ClinicEntity clinic;

    @Column(nullable = false)
    private Integer dayOfWeek; // 1-7 (Lunes-Domingo)

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private Boolean isActive = true;
}