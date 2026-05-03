package com.example.wasteapp.reference;

import com.example.wasteapp.reference.dto.WasteTypeCreateRequest;
import com.example.wasteapp.reference.dto.WasteTypeResponse;
import com.example.wasteapp.reference.dto.WasteTypeUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * KLASA: WasteTypeAdminController
 * SVRHA: ADMIN CRUD endpointi za vrste otpada.
 * NAPOMENA: Security pravilo ćemo postaviti u SecurityConfig (/api/admin/** -> ADMIN).
 */
@RestController
@RequestMapping("/api/admin/reference/waste-types")
public class WasteTypeAdminController {

    private final WasteTypeService service;

    public WasteTypeAdminController(WasteTypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<WasteTypeResponse> listAll() {
        return service.listAll().stream().map(WasteTypeResponse::new).toList();
    }

    @GetMapping("/{id}")
    public WasteTypeResponse get(@PathVariable Long id) {
        return new WasteTypeResponse(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<WasteTypeResponse> create(@Valid @RequestBody WasteTypeCreateRequest req) {
        WasteType created = service.create(req);
        return ResponseEntity.ok(new WasteTypeResponse(created));
    }

    @PutMapping("/{id}")
    public WasteTypeResponse update(@PathVariable Long id, @Valid @RequestBody WasteTypeUpdateRequest req) {
        return new WasteTypeResponse(service.update(id, req));
    }

    /**
     * Realno: radimo soft delete (active=false)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
