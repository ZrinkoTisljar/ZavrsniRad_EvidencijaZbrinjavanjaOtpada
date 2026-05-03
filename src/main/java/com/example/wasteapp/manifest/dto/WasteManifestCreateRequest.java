package com.example.wasteapp.manifest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO KLASA: WasteManifestCreateRequest
 *SVRHA: Podaci koje admin šalje ko generiranja prateceg lista
 * */
public class WasteManifestCreateRequest {

    @NotNull
    private Long workOrderId;

    @Size(max = 500)
    private String note;

    public Long getWorkOrderId() { return workOrderId; }
    public void setWorkOrderId(Long workOrderId) { this.workOrderId = workOrderId; }

    public String getNote() { return note; }
    public void  setNote(String note) { this.note = note; }
}
