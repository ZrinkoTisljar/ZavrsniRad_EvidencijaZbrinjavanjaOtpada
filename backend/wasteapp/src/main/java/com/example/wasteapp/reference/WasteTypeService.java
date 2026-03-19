package com.example.wasteapp.reference;

import com.example.wasteapp.reference.dto.WasteTypeCreateRequest;
import com.example.wasteapp.reference.dto.WasteTypeUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * KLASA: WasteTypeService
 * SVRHA: Poslovna logika za reference data (vrste otpada).
 */
@Service
public class WasteTypeService {

    private final WasteTypeRepository repo;

    public WasteTypeService(WasteTypeRepository repo) {
        this.repo = repo;
    }

    /**
     * Dohvat svih aktivnih vrsta otpada (za korisnike/forme).
     */
    public List<WasteType> listActive() {
        return repo.findAllByActiveTrueOrderByNameAsc();
    }

    /**
     * Admin: dohvat svih (aktivnih i neaktivnih).
     */
    public List<WasteType> listAll() {
        return repo.findAll();
    }

    public WasteType getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("WasteType not found: " + id));
    }

    @Transactional
    public WasteType create(WasteTypeCreateRequest req) {
        // business rule: code unique (case-sensitive možeš kasnije normalizirati)
        if (repo.existsByCode(req.getCode())) {
            throw new IllegalArgumentException("WasteType code already exists: " + req.getCode());
        }

        WasteType wt = new WasteType();
        wt.setCode(req.getCode());
        wt.setName(req.getName());
        wt.setDescription(req.getDescription());
        wt.setActive(true);

        return repo.save(wt);
    }

    @Transactional
    public WasteType update(Long id, WasteTypeUpdateRequest req) {
        WasteType wt = getById(id);

        wt.setName(req.getName());
        wt.setDescription(req.getDescription());
        wt.setActive(req.isActive());

        return repo.save(wt);
    }

    @Transactional
    public void delete(Long id) {
        // Realno: radije "disable" nego hard delete
        WasteType wt = getById(id);
        wt.setActive(false);
        repo.save(wt);
    }
}
