package com.example.wasteapp.reference.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO KLASA: WasteTypeCreateRequest
 * SVRHA: Podaci koje admin šalje kod kreiranja vrste otpada.
 */
public class WasteTypeCreateRequest {

    @NotBlank
    @Size(max = 50)
    private String code;

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 500)
    private String description;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
