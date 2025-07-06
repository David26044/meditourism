package com.meditourism.meditourism.contactForm.dto;

import com.meditourism.meditourism.clinic.dto.ClinicDTO;
import com.meditourism.meditourism.treatment.dto.TreatmentDTO;
import com.meditourism.meditourism.treatment.entity.TreatmentEntity;
import com.meditourism.meditourism.user.dto.UserResponseDTO;
import com.meditourism.meditourism.contactForm.entity.ContactFormEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ContactFormResponseDTO {

    private Long id;
    private UserResponseDTO user;
    private TreatmentDTO treatment;
    private ClinicDTO clinic;
    private String email;
    private String message;
    private LocalDateTime date;

    public ContactFormResponseDTO(Long id, UserResponseDTO user, TreatmentDTO treatment, String email, String message, LocalDateTime date, ClinicDTO clinic) {
        this.id = id;
        this.user = user;
        this.treatment = treatment;
        this.clinic = clinic;
        this.email = email;
        this.message = message;
        this.date = date;
    }

    public ContactFormResponseDTO(ContactFormEntity entity) {
        this.id = entity.getId();
        this.user = entity.getUser() != null ? new UserResponseDTO(entity.getUser()) : null;
        this.treatment = new TreatmentDTO(entity.getTreatment());
        this.clinic = new ClinicDTO(entity.getPreferredClinic());
        this.email = entity.getEmail();
        this.message = entity.getMessage();
        this.date = entity.getDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserResponseDTO getUser() {
        return user;
    }

    public void setUser(UserResponseDTO user) {
        this.user = user;
    }

    public TreatmentDTO getTreatment() {
        return treatment;
    }

    public void setTreatment(TreatmentDTO treatment) {
        this.treatment = treatment;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public static List<ContactFormResponseDTO> fromEntityList(List<ContactFormEntity> entityList) {
        List<ContactFormResponseDTO> dtoList = new ArrayList<>();
        for (ContactFormEntity entity : entityList) {
            dtoList.add(new ContactFormResponseDTO(entity));
        }
        return dtoList;
    }

}
