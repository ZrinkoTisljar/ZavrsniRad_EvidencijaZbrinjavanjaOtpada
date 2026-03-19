package com.example.wasteapp.reference;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * KLASA: WasteType (JPA Entity)
 * SVRHA: Predstavlja vrstu otpada (reference data) koju admin održava.
 * TABLICA: waste_types
 */
@Entity
@Table(name = "waste_types")
public class WasteType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Kod je jedinstveni "ključ" vrste otpada.
     * Primjeri: PLASTIC, PAPER, BIO, GLASS
     */
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    /**
     * Naziv za prikaz u UI-u.
     * Primjer: "Plastika"
     */
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    /**
     * Soft-aktivacija: ako je false, ne brišemo red iz baze,
     * nego ga samo sakrijemo iz aktivnih lista.
     */
    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public WasteType() {
        // PROGRAMSKI ENTITET: konstruktor
        // SVRHA: automatski postavi createdAt kod kreiranja instance
        this.createdAt = LocalDateTime.now();
    }

    // --- GETTERI/SETTERI ---

    public Long getId() { return id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
