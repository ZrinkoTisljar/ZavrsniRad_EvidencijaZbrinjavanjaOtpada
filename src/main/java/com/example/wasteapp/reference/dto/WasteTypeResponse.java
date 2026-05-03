package com.example.wasteapp.reference.dto;

import com.example.wasteapp.reference.WasteType;

/**
 * DTO KLASA: WasteTypeResponse
 * SVRHA: JSON koji vraćamo klijentu (ne vraćamo cijeli entity direktno).
 */
public class WasteTypeResponse {
    private Long id;
    private String code;
    private String name;
    private String description;
    private boolean active;

    public WasteTypeResponse(WasteType wt) {
        this.id = wt.getId();
        this.code = wt.getCode();
        this.name = wt.getName();
        this.description = wt.getDescription();
        this.active = wt.isActive();
    }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isActive() { return active; }
}
