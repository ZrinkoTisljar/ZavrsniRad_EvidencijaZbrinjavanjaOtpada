package com.example.wasteapp.reference;

import com.example.wasteapp.reference.dto.CollectionPointCreateRequest;
import com.example.wasteapp.reference.dto.CollectionPointResponse;
import com.example.wasteapp.reference.dto.CollectionPointUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * KLASA: CollectionPointAdminController
 * SVRHA: ADMIN CRUD za lokacije.
 *
 *  Ovo je REST kontroler koji omogucuje administratoru:
 *  -pregled svih lokacija
 *  -dohvat pojedine lokacije
 *  -kreiranje nove lokacije
 *  -azuriranje postojece lokacije
 *  -brisanje lokacije
 *
 *  Svi endpointi su dostupni samo ADMIN korisnicima (securityConfig)
 */

@RestController
@RequestMapping("/api/admin/reference/collection-points")
public class CollectionPointAdminController {

    // Service sloj koji sadrzi poslovnu logiku.
    private final CollectionPointService service;

    // Konstruktor injection - najbolja praksa u Springu
    public CollectionPointAdminController(CollectionPointService service) {
        this.service = service;
    }

    /**
     * GET /api/admin/reference/collection-points
     *
     * Vraca listu svih collection pointova.
     *
     * -Poziv service.listAll()
     * -Svaki entitet pretvara u Response DTO
     * -Stream + map + toList = cista i funkcionalna transformacija
     * */
    @GetMapping
    public List<CollectionPointResponse> listAll(){
        return service.listAll().stream().map(CollectionPointResponse::new).toList();
    }

    /**
     * Get /api/admin/reference/collection-points/{id}
     *
     * Dohvaca jedan collection point prema ID-u.
     *
     * @PathVariable Long id - cita ID iz URL-a
     * service.getBy(id) - baca gresku ako ne postoji (najcesce 404)
     * Response DTO vraca samo podatke potrebne klijentu
     * */
    @GetMapping("/{id}")
    public CollectionPointResponse get(@PathVariable Long id){
        return new CollectionPointResponse(service.getById(id));
    }


    /**
     * POST /api/admin/reference/collection-points
     *
     * Kreira novi collection point.
     *
     * @Valid - aktivira Bean Validation nad DTO-om
     * @RequestBody - JSON-> Java object
     *
     * ResponseEntity.ok(...) - vraca 200 OK + tijelo
     * (alternativa bi bila 201 Created)
     * */
    @PostMapping
    public ResponseEntity<CollectionPointResponse> create(@Valid @RequestBody CollectionPointCreateRequest req){
        return ResponseEntity.ok(new CollectionPointResponse(service.create(req)));
    }

    /**
     * PUT /api/admin/reference/collection-points/{id}
     *
     * Azurira postojeci collection point.
     *
     * PUT se koristi za "full update" - zamjena svih polja.
     *
     * @Valid - validavija inputa
     * service.update(id, req) - poslovna logika + spremanje u bazu
     * */
    @PutMapping("/{id}")
    public CollectionPointResponse update(@PathVariable Long id, @Valid @RequestBody CollectionPointUpdateRequest req){
        return new CollectionPointResponse(service.update(id, req));
    }


    /**
     * DELETE /api/admin/reference/colection-points/{id}
     *
     * Brise lokaciju.
     *
     * service.delete(id) - moze biti soft delete ili hard delete
     *
     * ResponceEntity.ok().build() - vraca 200 OK bez tijela
     * */
    @DeleteMapping("/{id}")
    public ResponseEntity<?>delete(@PathVariable Long id){
        service.delete(id);
        return  ResponseEntity.ok().build();
    }
 }
