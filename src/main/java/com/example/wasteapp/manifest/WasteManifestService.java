package com.example.wasteapp.manifest;


import com.example.wasteapp.manifest.dto.WasteManifestCreateRequest;
import com.example.wasteapp.user.User;
import com.example.wasteapp.user.UserRepository;
import com.example.wasteapp.workorder.WorkOrder;
import com.example.wasteapp.workorder.WorkOrderRepository;
import com.example.wasteapp.workorder.WorkOrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.plaf.PanelUI;
import java.time.LocalDateTime;
import java.util.IllegalFormatWidthException;
import java.util.List;

/**
 * KLASA: WasteManifestService
 * SVRHA: Poslovna logika za pratece listove.
 * */
@Service
public class WasteManifestService {

    private final WasteManifestRepository manifestRepo;
    private final WorkOrderRepository workOrderRepo;
    private final UserRepository userRepo;

    public WasteManifestService(
            WasteManifestRepository manifestRepo,
            WorkOrderRepository workOrderRepo,
            UserRepository userRepo
    ) {
        this.manifestRepo = manifestRepo;
        this.workOrderRepo = workOrderRepo;
        this.userRepo = userRepo;
    }

    /**
     * ADMIN: generira prateci list za postojeci radni nalog.
     * Pravilo:
     * - radni nalog mora postojati
     * - ne smije vec imati manifest
     * - ?? nalog mora biti SCHEDULED ili COMPLETED ??
     * */
    @Transactional
    public WasteManifest create(WasteManifestCreateRequest req){
        WorkOrder wo = workOrderRepo.findById(req.getWorkOrderId())
                .orElseThrow(() -> new IllegalArgumentException("WorkOrder not found"));

        if (manifestRepo.findByWorkOrderId(wo.getId()).isPresent()){
            throw new IllegalArgumentException("Manifest already exists for this WorkOrder");
        }

        if(wo.getStatus() != WorkOrderStatus.SCHEDULED && wo.getStatus() != WorkOrderStatus.COMPLETED) {
            throw new IllegalArgumentException("Manifest can be created only for SCHEDULED or COMPLETED work orders");
        }

        WasteManifest wm = new WasteManifest();
        wm.setWorkOrder(wo);
        wm.setIssuedAt(LocalDateTime.now());
        wm.setNote(req.getNote());

        // Jednostavan jedinstveni broj dokumenata
        // Primjer: WM-2026-<workOrderId>
        wm.setManifestNumber("WM-" + LocalDateTime.now().getYear() + "-" + String.format("%06d", wo.getId()));

        return manifestRepo.save(wm);
    }

    public List<WasteManifest> listAll() {
        return manifestRepo.findAllByOrderByIssuedAtDesc();
    }

    public WasteManifest getById(Long id) {
        return manifestRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("WasteManifest not found"));
    }

    public List<WasteManifest> listMine(String userEmail) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return manifestRepo.findAllByWorkOrderUserIdOrderByIssuedAtDesc(user.getId());
    }

    /**
     * PROGRAMSKI ENTITET: servisna metoda
     * SVRHA:
     * - ADMIN smije dohvatiti bilo koji manifest
     * - USER smije dohvatiti samo svoj manifest
     *
     * PARAMETRI:
     * - manifestId: ID manifesta
     * - currentUserEmail: email prijavljenog korisnika iz JWT tokena
     * - isAdmin: true ako korisnik ima ADMIN ulogu
     */
    public WasteManifest getAccessibleManifest(Long manifestId, String currentUserEmail, boolean isAdmin) {

        // Ako je ADMIN, smije pristupiti bilo kojem manifestu
        if (isAdmin) {
            return manifestRepo.findById(manifestId)
                    .orElseThrow(() -> new IllegalArgumentException("WasteManifest not found"));
        }

        // Ako je obični USER, smije dohvatiti samo svoj manifest
        return manifestRepo.findByIdAndWorkOrderUserEmail(manifestId, currentUserEmail)
                .orElseThrow(() -> new IllegalArgumentException("WasteManifest not found or access denied"));
    }
}
