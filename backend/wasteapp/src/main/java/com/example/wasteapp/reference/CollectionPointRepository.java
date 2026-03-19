package com.example.wasteapp.reference;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * INTERFACE: CollectionPointRepository
 * SVRHA: JPA repository za CollectionPoint.
 */
public interface CollectionPointRepository extends JpaRepository<CollectionPoint, Long> {

    // Javni prikaz: samo aktivne, sortirano po nazivu
    List<CollectionPoint> findAllByActiveTrueOrderByNameAsc();

    // Korisno za filtriranje po gradu (aktivne)
    List<CollectionPoint> findAllByActiveTrueAndCityIgnoreCaseOrderByNameAsc(String city);
}
