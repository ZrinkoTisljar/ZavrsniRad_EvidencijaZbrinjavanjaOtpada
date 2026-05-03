package com.example.wasteapp.report.dto;


import java.math.BigDecimal;

/**
 * DTO KLASA: wasteTypeReportRow
 * SVRHA: Jedan red izvjesca "ukupna kolicina otpada po vrsti".
 * */
public class WasteTypeReportRow {

    private String wasteTypeCode;
    private String wasteTypeName;
    private BigDecimal totalQuantity;

    public WasteTypeReportRow(String wasteTypeCode, String wasteTypeName, BigDecimal totalQuantity) {
        this.wasteTypeCode = wasteTypeCode;
        this.wasteTypeName = wasteTypeName;
        this.totalQuantity = totalQuantity;
    }

    public String getWasteTypeName() {
        return wasteTypeName;
    }

    public String getWasteTypeCode() {
        return wasteTypeCode;
    }

    public BigDecimal getTotalQuantity() {
        return totalQuantity;
    }
}
