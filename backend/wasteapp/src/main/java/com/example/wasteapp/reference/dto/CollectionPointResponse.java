package com.example.wasteapp.reference.dto;

import com.example.wasteapp.reference.CollectionPoint;

import java.math.BigDecimal;

/**
 *  DTO KLASA: CollectionPointResponse
 *  SVRHA: JSON odgovor preama klijenu (UI)
 * */

public class CollectionPointResponse {

    private Long id;
    private String name;
    private String street;
    private String city;
    private String postalCode;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private boolean active;

    public CollectionPointResponse(CollectionPoint cp){
        this.id= cp.getId();
        this.name= cp.getName();
        this.street=cp.getStreet();
        this.city=cp.getCity();
        this.postalCode=cp.getPostalCode();
        this.latitude= cp.getLatitude();;
        this.longitude=cp.getLongitude();
        this.active= cp.isActive();

    }

    public Long getId(){ return id;}
    public String getName(){return name;}
    public String getStreet(){return street;}
    public String getCity(){return city;}
    public String getPostalCode(){return postalCode;}
    public BigDecimal getLatitude(){return latitude;}
    public BigDecimal getLongitude(){return longitude;}
    public boolean isActive(){return active;}
}
