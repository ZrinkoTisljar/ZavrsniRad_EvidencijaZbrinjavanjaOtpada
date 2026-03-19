package com.example.wasteapp.reference;

import com.example.wasteapp.reference.dto.WasteTypeResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * KLASA: WasteTypePublicController
 * SVRHA: Endpoint za dohvat aktivnih vrsta otpada (za forme u SPA-u).
 * Ovdje vraćamo samo aktivne vrste.
 */
@RestController
@RequestMapping("/api/reference/waste-types")
public class WasteTypePublicController {

    private final WasteTypeService service;

    public WasteTypePublicController(WasteTypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<WasteTypeResponse> listActive() {
        return service.listActive().stream().map(WasteTypeResponse::new).toList();
    }
}
