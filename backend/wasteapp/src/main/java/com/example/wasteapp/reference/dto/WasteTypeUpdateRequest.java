package com.example.wasteapp.reference.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO KLASA: WasteTypeUpdateRequest
 * SVRHA: Podaci za izmjenu (admin).
 * Napomena: code obično ne mijenjamo (da ostane stabilan ključ),
 * ali može se dozvoliti ako želimo.
 */
public class WasteTypeUpdateRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 500)
    private String description;

    private boolean active = true;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
