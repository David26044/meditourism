package com.meditourism.meditourism.contactForm.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ContactFormRequestDTO {

    @NotNull(message = "El id del usuario no peude ser nulo")
    private Long userId;

    @NotNull(message = "El id del tratamiento no puede ser nulo")
    private Long treatmentId;

    @NotBlank(message = "Debes de ingresar un correo")
    private String email;

    @NotBlank(message = "El mensaje no puede estar vacio")
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}