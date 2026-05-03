package com.example.wasteapp.reference;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * INTERFACE: WasteTypeRepository
 * SVRHA: Spring Data JPA automatski generira SQL upite za entity WasteType.
 */
public interface WasteTypeRepository extends JpaRepository<WasteType, Long> {

    Optional<WasteType> findByCode(String code);

    boolean existsByCode(String code);

    // Korisno za javni prikaz (samo aktivne)
    List<WasteType> findAllByActiveTrueOrderByNameAsc();
}
