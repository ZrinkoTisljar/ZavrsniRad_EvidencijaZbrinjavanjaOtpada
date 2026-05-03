package com.example.wasteapp.report.dto;


import java.math.BigDecimal;

/**
 * DTO KLASA: WasteByCityReportRow
 * SVRHA: Jedan red izvješća "ukupna količina otpada po gradu".
 * */
public class WasteByCityReportRow {

    private String city; // polje klase koje čuva naziv grada
    private BigDecimal totalQuantity;

    // konstruktor sa dva parametra koja se dobivaju iz metode @query iz interface WorkOrderRepository
    public WasteByCityReportRow(String city, BigDecimal totalQuantity) {
        this.city = city;
        this.totalQuantity = totalQuantity;
    }

    public String getCity() {
        return city;
    }

    public BigDecimal getTotalQuantity() {
        return totalQuantity;
    }

}
