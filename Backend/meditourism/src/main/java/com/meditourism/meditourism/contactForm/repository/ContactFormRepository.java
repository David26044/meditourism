package com.meditourism.meditourism.contactForm.repository;

import com.meditourism.meditourism.contactForm.entity.ContactFormEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContactFormRepository extends JpaRepository<ContactFormEntity, Long> {

    Optional<List<ContactFormEntity>> findAllByTreatmentId(Long id);

    Optional<List<ContactFormEntity>> findAllByUserId(Long id);
}
