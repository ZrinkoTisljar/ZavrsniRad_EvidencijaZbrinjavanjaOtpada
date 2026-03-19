package com.example.wasteapp.workorder.dto;


import com.example.wasteapp.workorder.QuantityUnit;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * DTO KLASA: WorkOrderCreateRequest
 * SVRHA: Podaci koje USER salje kod kreiranja radnog naloga
 *
 * */


public class WorkOrderCreateRequest {

    @NotNull
    private Long wasteTypeId;

    @NotNull
    private Long collectionPointId;

    @NotNull
    @DecimalMin(value = "0.001", message = "Količina mora biti veća od 0.")
    @Digits(integer = 9, fraction = 3, message = "Maks 9 znamenki prije točke i 3 decimale.")
    private BigDecimal quantity;

    @NotNull
    private QuantityUnit unit;

    @Size(max = 500)
    private String note;

    public Long getWasteTypeId() { return wasteTypeId; }
    public void setWasteTypeId(Long wasteTypeId) { this.wasteTypeId = wasteTypeId; }

    public Long getCollectionPointId() { return collectionPointId; }
    public void setCollectionPointId(Long collectionPointId) { this.collectionPointId = collectionPointId; }

    public BigDecimal getQuantity() { return quantity; }
    public  void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public String getNote() {return note; }
    public  void setNote(String note) { this.note = note; }

    public QuantityUnit getUnit() { return unit; }
    public void setUnit(QuantityUnit unit) { this.unit = unit; }
}
