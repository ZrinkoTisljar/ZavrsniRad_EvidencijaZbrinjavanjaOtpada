package com.example.wasteapp.manifest;

import com.example.wasteapp.workorder.WorkOrder;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 *  KLASA: WasteManifest (JPA Entity)
 *  SVRHA: Predstavlja pateći list za otpad.
 *  TABLICA: waste_manifests
 */

@Entity
@Table(name = "waste_manifests")
public class WasteManifest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Jedinstveni broj prateceg lista.
     * Primjer: WM-2026-000001
     * */
    @Column(name =  "manifest_number", nullable = false, unique = true, length = 50)
    private String manifestNumber;

    /**
     * Veza na radni nalog iz kojeg je nastao prateci list.
     * Jedan work order -> jedan manifest.
     */

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "work_order_id", nullable = false, unique = true)
    private WorkOrder workOrder;

    /**
     * Datum izdavanja pratecek lista.
     */
    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    /**
     * Dodatna napomena dokumenta.
     */
    @Column(name = "note",length = 500)
    private String note;

    /**
     * Datum kreiranja entiteta u bazi.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public WasteManifest(){
        //PROGRAMSKI ENTITET: konstruktor
        //SVRHA: automatski postavlja vrijeme kreiranja dokumenta
        this.createdAt = LocalDateTime.now();
        this.issuedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }

    public String getManifestNumber() { return manifestNumber; }
    public void setManifestNumber(String manifestNumber) { this.manifestNumber = manifestNumber; }

    public WorkOrder getWorkOrder(){ return workOrder; }
    public void setWorkOrder(WorkOrder workOrder) { this.workOrder = workOrder; }

    public LocalDateTime getIssuedAt() { return issuedAt; }
    public void setIssuedAt(LocalDateTime issuedAt) { this.issuedAt= issuedAt; }

    public String getNote(){ return note; }
    public void setNote(String note) { this.note = note; }

    public  LocalDateTime getCreatedAt() { return createdAt; }
}
