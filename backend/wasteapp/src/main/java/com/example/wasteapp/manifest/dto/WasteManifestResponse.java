package com.example.wasteapp.manifest.dto;

import com.example.wasteapp.manifest.WasteManifest;

import javax.print.DocFlavor;

/**
 * DTO KLASA: WasteManifestResponse
 * SVRHA: JSON odgovor za prikaz prateceg lista.
 * */
public class WasteManifestResponse {

    private Long id;
    private String manifestNumber;
    private Long workOrderId;
    private String issuedAt;
    private String note;

    private String wasteTypeName;
    private String collectionPointName;
    private String collectionPointCity;
    private String userEmail;
    private String quantity;
    private String unit;
    private String workOrderStatus;

    public WasteManifestResponse(WasteManifest wm) {
        this.id = wm.getId();
        this.manifestNumber = wm.getManifestNumber();
        this.workOrderId = wm.getWorkOrder().getId();
        this.issuedAt = wm.getIssuedAt().toString();
        this.note = wm.getNote();

        this.wasteTypeName = wm.getWorkOrder().getWasteType().getName();
        this.collectionPointName = wm.getWorkOrder().getCollectionPoint().getName();
        this.collectionPointCity = wm.getWorkOrder().getCollectionPoint().getCity();
        this.userEmail = wm.getWorkOrder().getUser().getEmail();
        this.quantity = wm.getWorkOrder().getQuantity().toString();
        this.unit = wm.getWorkOrder().getUnit().name();
        this.workOrderStatus = wm.getWorkOrder().getStatus().name();
    }

    public Long getId() {return id; }
    public String getManifestNumber() { return manifestNumber; }
    public Long getWorkOrderId() { return workOrderId; }
    public String getIssuedAt() { return issuedAt; }
    public String getNote() { return note;}
    public String getWasteTypeName() { return wasteTypeName; }
    public String getCollectionPointName() { return collectionPointName; }
    public String getCollectionPointCity() { return collectionPointCity; }
    public String getUserEmail() { return userEmail; }
    public String getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public String getWorkOrderStatus() { return workOrderStatus; }


}
