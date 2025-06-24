package com.meditourism.meditourism.contactForm.service;

import com.meditourism.meditourism.contactForm.dto.ContactFormRequestDTO;
import com.meditourism.meditourism.contactForm.dto.ContactFormResponseDTO;

import java.util.List;

public interface IContactFormService {

    List<ContactFormResponseDTO> getAllContactForms();
    List<ContactFormResponseDTO> getAllContactFormsByTreatmentId(Long id);
    ContactFormResponseDTO getContactFormById(Long id);
    ContactFormResponseDTO saveContactForm(ContactFormRequestDTO contactFormRequestDTO);
    List<ContactFormResponseDTO> getContactFormByUserId(Long id);
    ContactFormResponseDTO updateContactForm(Long id, ContactFormRequestDTO contactFormRequestDTO);
    ContactFormResponseDTO deleteContactForm(Long id);

}
