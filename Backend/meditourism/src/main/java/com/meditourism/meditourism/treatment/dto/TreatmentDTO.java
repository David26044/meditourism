package com.meditourism.meditourism.treatment.dto;

import jakarta.validation.constraints.NotBlank;

public class TreatmentDTO {

    @NotBlank(message = "El nombre no puede estar vacio")
    private String name;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
