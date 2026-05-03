package com.example.wasteapp.manifest;


import com.example.wasteapp.manifest.dto.WasteManifestCreateRequest;
import com.example.wasteapp.manifest.dto.WasteManifestResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 *  CONTROLLER: AdminWasteManifestControler
 *  SVRHA: ADMIN upravlja pratecim listovima.
 * */
@RestController
@RequestMapping("/api/admin/manifests")
public class AdminWasteManifestController {

    public final WasteManifestService service;

    public AdminWasteManifestController(WasteManifestService service) {
        this.service = service;
    }

    @PostMapping
    public WasteManifestResponse create(@Valid @RequestBody WasteManifestCreateRequest req) {
        return new WasteManifestResponse(service.create(req));
    }

    @GetMapping
    public List<WasteManifestResponse> listAll() {
        return service.listAll().stream().map(WasteManifestResponse::new).toList();
    }

    @GetMapping("/{id}")
    public WasteManifestResponse getById(@PathVariable Long id) {
        return new WasteManifestResponse(service.getById(id));
    }
}
