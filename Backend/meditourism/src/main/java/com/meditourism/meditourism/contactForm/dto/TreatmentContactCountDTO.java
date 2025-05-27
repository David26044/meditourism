package com.meditourism.meditourism.contactForm.dto;

public class TreatmentContactCountDTO {
    private Long treatmentId;
    private String treatmentName;
    private Long contactCount;

    public TreatmentContactCountDTO(Long treatmentId, String treatmentName, Long contactCount) {
        this.treatmentId = treatmentId;
        this.treatmentName = treatmentName;
        this.contactCount = contactCount;
    }

    public Long getTreatmentId() {
        return treatmentId;
    }

    public String getTreatmentName() {
        return treatmentName;
    }

    public Long getContactCount() {
        return contactCount;
    }
}
