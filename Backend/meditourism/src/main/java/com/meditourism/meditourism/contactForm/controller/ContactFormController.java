package com.meditourism.meditourism.contactForm.controller;

import com.meditourism.meditourism.contactForm.dto.ContactFormRequestDTO;
import com.meditourism.meditourism.contactForm.dto.ContactFormResponseDTO;
import com.meditourism.meditourism.contactForm.dto.TreatmentContactCountDTO;
import com.meditourism.meditourism.contactForm.service.ContactFormService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/contact-forms")
public class ContactFormController {

    @Autowired
    private ContactFormService contactFormService;

    @GetMapping
    public ResponseEntity<List<ContactFormResponseDTO>> getAllContactForms() {
        return ResponseEntity.ok(contactFormService.getAllContactForms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactFormResponseDTO> getContactFormById(@PathVariable Long id) {
        return ResponseEntity.ok(contactFormService.getContactFormById(id));
    }

    @GetMapping("/treatment/{treatmentId}")
    public ResponseEntity<List<ContactFormResponseDTO>> getAllByTreatmentId(@PathVariable Long treatmentId) {
        return ResponseEntity.ok(contactFormService.getAllContactFormsByTreatmentId(treatmentId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ContactFormResponseDTO>> getAllByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(contactFormService.getContactFormByUserId(userId));
    }

    @GetMapping("/treatment-contact-count")
    public ResponseEntity<List<TreatmentContactCountDTO>> getTreatmentContactCounts() {
        return ResponseEntity.ok(contactFormService.getTreatmentContactCounts());
    }


    @PostMapping
    @PreAuthorize("permitAll()")  // Permitir acceso público
    public ResponseEntity<ContactFormResponseDTO> createContactForm(@RequestBody @Valid ContactFormRequestDTO dto) {
        ContactFormResponseDTO savedContactForm = contactFormService.saveContactForm(dto);
        return ResponseEntity
                .created(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("{/id}")
                        .buildAndExpand(savedContactForm.getId())
                        .toUri())
                .body(savedContactForm);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ContactFormResponseDTO> updateContactForm(
            @PathVariable Long id,
            @RequestBody ContactFormRequestDTO dto) {
        return ResponseEntity.ok(contactFormService.updateContactForm(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ContactFormResponseDTO> deleteContactForm(@PathVariable Long id) {
        return ResponseEntity.ok(contactFormService.deleteContactForm(id));
    }
}
