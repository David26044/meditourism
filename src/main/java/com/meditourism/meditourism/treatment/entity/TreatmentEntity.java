package com.meditourism.meditourism.treatment.entity;

import jakarta.persistence.*;

@Entity
public class TreatmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String description;

    @Column(name = "price_range", nullable = false)
    private String priceRange;
}
