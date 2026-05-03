package com.example.wasteapp.reference.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * DTO KLASA: CollectionPointCreateRequest
 * SVRHA: Podaci za kreiranje lokacije (admin).
 */
public class CollectionPointCreateRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String street;

    @NotBlank
    @Size(max = 100)
    private String city;

    @Size(max = 20)
    private String postalCode;

    private BigDecimal latitude;
    private BigDecimal longitude;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }

    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
}
