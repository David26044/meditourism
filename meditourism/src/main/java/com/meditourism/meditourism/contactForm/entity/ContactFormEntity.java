package com.meditourism.meditourism.contactForm.entity;

import com.meditourism.meditourism.treatment.entity.TreatmentEntity;
import com.meditourism.meditourism.user.entity.UserEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "contact_form")
public class ContactFormEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contact_form_seq")
    @SequenceGenerator(name = "contact_form_seq", sequenceName = "contact_form_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)  // Changed to nullable = true
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "treatment_id", nullable = false)
    private TreatmentEntity treatment;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String message;

    @Column(name = "full_name")
    private String fullName;

    @Column
    private String phone;

    @Column(name = "inquiry_type")
    private String inquiryType;

    @Column(name = "preferred_clinic")
    private String preferredClinic;

    @Column(name = "accept_terms")
    private Boolean acceptTerms = true;

    @Column(name = "accept_marketing")
    private Boolean acceptMarketing = false;

    @Column
    @CreationTimestamp
    private LocalDateTime date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public TreatmentEntity getTreatment() {
        return treatment;
    }

    public void setTreatment(TreatmentEntity treatment) {
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
