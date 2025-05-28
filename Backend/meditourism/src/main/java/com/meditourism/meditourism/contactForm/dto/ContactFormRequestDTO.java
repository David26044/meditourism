package com.meditourism.meditourism.contactForm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ContactFormRequestDTO {

    // Hacer userId opcional para permitir formularios de usuarios no autenticados
    private Long userId;

    @NotNull(message = "El id del tratamiento no puede ser nulo")
    private Long treatmentId;

    @NotBlank(message = "El nombre completo es requerido")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String fullName;

    @NotBlank(message = "Debes ingresar un correo")
    @Email(message = "El formato del email no es válido")
    private String email;

    private String phone;

    @NotBlank(message = "El tipo de consulta es requerido")
    private String inquiryType;

    private String preferredClinic;

    @NotBlank(message = "El mensaje no puede estar vacío")
    @Size(min = 10, max = 500, message = "El mensaje debe tener entre 10 y 500 caracteres")
    private String message;

    @NotNull(message = "Debes aceptar los términos y condiciones")
    private Boolean acceptTerms;

    private Boolean acceptMarketing = false;

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInquiryType() {
        return inquiryType;
    }

    public void setInquiryType(String inquiryType) {
        this.inquiryType = inquiryType;
    }

    public String getPreferredClinic() {
        return preferredClinic;
    }

    public void setPreferredClinic(String preferredClinic) {
        this.preferredClinic = preferredClinic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getAcceptTerms() {
        return acceptTerms;
    }

    public void setAcceptTerms(Boolean acceptTerms) {
        this.acceptTerms = acceptTerms;
    }

    public Boolean getAcceptMarketing() {
        return acceptMarketing;
    }

    public void setAcceptMarketing(Boolean acceptMarketing) {
        this.acceptMarketing = acceptMarketing;
    }

}