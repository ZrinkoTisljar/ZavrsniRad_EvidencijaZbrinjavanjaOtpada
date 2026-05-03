package com.example.wasteapp.reference;


import com.example.wasteapp.reference.dto.CollectionPointResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * KLASA: CollectionPublicControler
 * SVRHA: javni dohvat aktivnih lokacija (za forme u SPA-u).
 */

@RestController
@RequestMapping("/api/reference/collection-points")
public class CollectionPointPublicController {

    private  final CollectionPointService service;

    public CollectionPointPublicController(CollectionPointService service) {
        this.service = service;
    }

    /**
     * Primjer:
     * GET /api/reference/collection-points
     * GET /api/reference/collection-points?city=Cakovec
     *      *
     */
    @GetMapping
    public List<CollectionPointResponse> listActive(@RequestParam(required = false)String city){
        return service.listActive(city).stream().map(CollectionPointResponse::new).toList();
    }
}
