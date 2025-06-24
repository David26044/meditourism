package com.meditourism.meditourism.contactForm.repository;

import com.meditourism.meditourism.contactForm.entity.ContactFormEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ContactFormRepository extends JpaRepository<ContactFormEntity, Long> {

    List<ContactFormEntity> findAllByTreatmentId(Long id);

    List<ContactFormEntity> findAllByUserId(Long id);

    @Query(value = "SELECT t.id AS treatmentId, t.name AS treatmentName, COUNT(cf.id) AS contactCount " +
            "FROM treatments t " +
            "LEFT JOIN contact_form cf ON t.id = cf.treatment_id " +
            "GROUP BY t.id, t.name",
            nativeQuery = true)
    List<Object[]> findTreatmentContactCounts();

}
