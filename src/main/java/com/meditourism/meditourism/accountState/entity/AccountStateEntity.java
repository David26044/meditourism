package com.meditourism.meditourism.accountState.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "account_states")
public class AccountStateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String state;

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public String getState() {
        return state;
    }

}

