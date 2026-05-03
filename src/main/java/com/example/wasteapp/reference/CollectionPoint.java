package com.example.wasteapp.reference;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * KLASA: CollectionPoint (JPA Entity)
 * SVRHA: Predstavlja lokaciju (reciklažno dvorište/odlagalište/punkt).
 * TABLICA: collection_points
 */
@Entity
@Table(name = "collection_points")
public class CollectionPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Naziv lokacije (npr. "Reciklažno dvorište Čakovec")
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    // Ulica i broj (opcionalno)
    @Column(name = "street", length = 255)
    private String street;

    // Grad/općina (obavezno)
    @Column(name = "city", nullable = false, length = 100)
    private String city;

    // Poštanski broj (opcionalno)
    @Column(name = "postal_code", length = 20)
    private String postalCode;

    /** // GPS koordinate (opcionalno)
    @Column(name = "latitude", precision = 10, scale = 7)
    private Double latitude;

    @Column(name = "longitude", precision = 10, scale = 7)
    private Double longitude;*/

    /**
     * GPS latitude (DECIMAL u bazi -> BigDecimal u Javi).
     * precision=10, scale=7 znači: ukupno 10 znamenki, 7 iza decimalne točke.
     */
    @Column(name = "latitude", precision = 10, scale = 7)
    private BigDecimal latitude;

    /**
     * GPS longitude (DECIMAL u bazi -> BigDecimal u Javi).
     */
    @Column(name = "longitude", precision = 10, scale = 7)
    private BigDecimal longitude;

    // Soft delete / aktivnost
    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public CollectionPoint() {
        // PROGRAMSKI ENTITET: konstruktor
        // SVRHA: automatski postavlja datum kreiranja objekta
        this.createdAt = LocalDateTime.now();
    }

    // --- GETTERI/SETTERI ---

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
/**
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }*/

public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }

    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
