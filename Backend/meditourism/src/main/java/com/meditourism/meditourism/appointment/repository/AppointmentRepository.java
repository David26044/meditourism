package com.meditourism.meditourism.appointment.repository;

import com.meditourism.meditourism.appointment.entity.AppointmentEntity;
import com.meditourism.meditourism.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {

    @Query("SELECT COUNT(a) > 0 FROM AppointmentEntity a " +
            "WHERE a.clinicTreatment.clinic.id = :clinicId " +
            "AND a.beginDate < :endDate " +
            "AND a.endDate > :beginDate")
    boolean existsOverlappingAppointment(Long clinicId, LocalDateTime beginDate, LocalDateTime endDate);

    @Query("SELECT a FROM AppointmentEntity a WHERE a.clinicTreatment.clinic.id = :clinicId AND a.beginDate >= :start AND a.endDate <= :end")
    List<AppointmentEntity> findAllByClinicIdAndDateRange(@Param("clinicId") Long clinicId,
                                                          @Param("start") LocalDateTime start,
                                                          @Param("end") LocalDateTime end);


    List<AppointmentEntity> findAllByUser(UserEntity user);

    List<AppointmentEntity> findAllByClinicTreatment_Clinic_Id(Long clinicId);

    List<AppointmentEntity> findAllByUserId(Long id);
}
