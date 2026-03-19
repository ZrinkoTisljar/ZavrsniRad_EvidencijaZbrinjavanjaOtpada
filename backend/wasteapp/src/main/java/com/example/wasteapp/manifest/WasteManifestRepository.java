package com.example.wasteapp.manifest;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * INTERFACE: WasteManifestRepository
 * SVRHA: Dohvat i spremanje pratecih listova.
 * */
public interface WasteManifestRepository extends JpaRepository<WasteManifest,Long> {

    Optional<WasteManifest> findByManifestNumber(String manifestNumber);

    Optional<WasteManifest> findByWorkOrderId(Long workOrderId);

    List<WasteManifest> findAllByWorkOrderUserIdOrderByIssuedAtDesc(Long userId);

    /**
     * USER sigurnost:
     * Pronađi manifest samo ako pripada korisniku s tim emailom.
     */
    Optional<WasteManifest> findByIdAndWorkOrderUserEmail(Long id, String email);

}
