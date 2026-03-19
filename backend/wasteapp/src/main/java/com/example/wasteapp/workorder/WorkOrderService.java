package com.example.wasteapp.workorder;

import com.example.wasteapp.reference.CollectionPoint;
import com.example.wasteapp.reference.CollectionPointRepository;
import com.example.wasteapp.reference.WasteType;
import com.example.wasteapp.reference.WasteTypeRepository;
import com.example.wasteapp.user.User;
import com.example.wasteapp.user.UserRepository;
import com.example.wasteapp.workorder.dto.WorkOrderCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * KLASA: WorkOrderService
 * SVRHA: Poslovna logika za radne naloge.
 * */
@Service
public class WorkOrderService {

    private final WorkOrderRepository workOrderRepo;
    private final UserRepository userRepo;
    private final WasteTypeRepository wasteTypeRepo;
    private final CollectionPointRepository collectionPointRepo;

    public WorkOrderService(
            WorkOrderRepository workOrderRepo,
            UserRepository userRepo,
            WasteTypeRepository wasteTypeRepo,
            CollectionPointRepository collectionPointRepo)
    {
        this.workOrderRepo = workOrderRepo;
        this.userRepo = userRepo;
        this.wasteTypeRepo = wasteTypeRepo;
        this.collectionPointRepo = collectionPointRepo;
    }

    /**
     * USER: kreiranje naloga.
     * */
    @Transactional
    public WorkOrder createForUser(String userEmail, WorkOrderCreateRequest req) {

        //1) nadi usera po emailu (email dolazi iz JWT tokena: auth.getName())
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 2) provjeri da reference podaci postoje i da su aktivni
        WasteType wt = wasteTypeRepo.findById(req.getWasteTypeId())
                .orElseThrow(() -> new IllegalArgumentException("WasteType not found"));

        if (!wt.isActive()){
            throw new IllegalArgumentException("WasteType is not active");
        }
        CollectionPoint cp = collectionPointRepo.findById(req.getCollectionPointId())
                .orElseThrow(() -> new IllegalArgumentException("CollectionPoint is not active"));

        if (!cp.isActive()) {
            throw new IllegalArgumentException("CollectionPoint is not active");
        }

        // 3) napravi WorkOrder
        WorkOrder wo = new WorkOrder();
        wo.setUser(user);
        wo.setWasteType(wt);
        wo.setCollectionPoint(cp);
        wo.setQuantity(req.getQuantity());
        wo.setUnit(req.getUnit());
        wo.setNote(req.getNote());
        //status/requestedAt/creqtedAt se postave u konstruktoru (CREATED)

        return workOrderRepo.save(wo);
    }

    /**
     *  USER: lista mojih naloga.
     * */
    public List<WorkOrder> listMine(String userEmail) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return workOrderRepo.findAllByUser_IdOrderByRequestedAtDesc(user.getId());
    }

    /**
     * ADMIN: filtriranje radnih naloga prema zadanim kriterijima.
     *
     * PROGRAMSKI ENTITET: servisna metoda
     * SVRHA:
     * - Prima opcionalne filter parametre
     * - Poziva repository metodu koja vraća filtrirane rezultate
     */
    public List<WorkOrder> filterAdmin(
            WorkOrderStatus status,
            String wasteTypeCode,
            String city,
            String userEmail
    ) {
        return workOrderRepo.filterAdmin(status, wasteTypeCode, city, userEmail);
    }

}
