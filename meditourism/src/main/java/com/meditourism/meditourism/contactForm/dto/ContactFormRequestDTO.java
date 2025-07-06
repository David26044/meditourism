package com.meditourism.meditourism.contactForm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ContactFormRequestDTO {

    @NotNull(message = "El id del usuario no puede ser nulo")
    private Long userId;

    @NotNull(message = "El id del tratamiento no puede ser nulo")
    private Long treatmentId;

    private Long preferredClinicId;

    @NotBlank(message = "Debes ingresar un correo")
    @Email(message = "El formato del email no es válido")
    private String email;

    @NotBlank(message = "El mensaje no puede estar vacío")
    @Size(min = 10, max = 500, message = "El mensaje debe tener entre 10 y 500 caracteres")
    private String message;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(Long treatmentId) {
        this.treatmentId = treatmentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPreferredClinic() {
        return preferredClinicId;
    }

    public void setPreferredClinic(long preferredClinicId) {
        this.preferredClinicId = preferredClinicId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}