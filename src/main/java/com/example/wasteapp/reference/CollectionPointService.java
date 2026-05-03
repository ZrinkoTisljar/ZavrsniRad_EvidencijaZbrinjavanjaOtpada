package com.example.wasteapp.reference;

import com.example.wasteapp.reference.dto.CollectionPointCreateRequest;
import com.example.wasteapp.reference.dto.CollectionPointUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * KLASA: CollectionPointService
 * SVRHA: Poslovna logika za lokacije (reference data).
 */
@Service
public class CollectionPointService {

    private final CollectionPointRepository repo;

    public CollectionPointService(CollectionPointRepository repo) {
        this.repo = repo;
    }

    public List<CollectionPoint> listActive(String city){
        //PROGRAMSKI ENTITET : metoda
        //SVRHA: vraća aktivne lokacije, opcionalnofiltrirane po gradu
        if (city==null || city.isBlank()){
            return repo.findAllByActiveTrueOrderByNameAsc();
        }
        return repo.findAllByActiveTrueAndCityIgnoreCaseOrderByNameAsc(city);

    }

    public List<CollectionPoint> listAll(){
        return repo.findAll();
    }

    public CollectionPoint getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("CollectionPoint not found: " + id));
    }
    @Transactional
    public CollectionPoint create(CollectionPointCreateRequest req) {
        CollectionPoint cp= new CollectionPoint();
        cp.setName(req.getName());
        cp.setStreet(req.getStreet());
        cp.setCity(req.getCity());
        cp.setPostalCode(req.getPostalCode());
        cp.setLatitude(req.getLatitude());
        cp.setLongitude(req.getLongitude());
        cp.setActive(true);

        return repo.save(cp);
    }

    @Transactional
    public CollectionPoint update(Long id, CollectionPointUpdateRequest req){
        CollectionPoint cp= getById(id);

        cp.setName(req.getName());
        cp.setStreet(req.getStreet());
        cp.setCity(req.getCity());
        cp.setPostalCode(req.getPostalCode());
        cp.setLatitude(req.getLatitude());
        cp.setLongitude(req.getLongitude());
        cp.setActive(req.isActive());

        return repo.save(cp);
    }

    @Transactional
    public void delete(Long id){
        // Soft delete: samo deaktiviramo lokaciju
        CollectionPoint cp = getById(id);
        cp.setActive(false);
        repo.save(cp);
    }
}
