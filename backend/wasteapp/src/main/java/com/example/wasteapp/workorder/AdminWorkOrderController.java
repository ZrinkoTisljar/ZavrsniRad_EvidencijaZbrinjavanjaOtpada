package com.example.wasteapp.workorder;

import com.example.wasteapp.workorder.dto.WorkOrderResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * CONTROLLER: AdminWorkOrderController
 * SVRHA: ADMIN upravljanje radnim nalozima.
 * */

@RestController
@RequestMapping("/api/admin/work-orders")
public class AdminWorkOrderController {

    private final WorkOrderRepository repo;
    private final WorkOrderService service;


    public AdminWorkOrderController(WorkOrderRepository repo, WorkOrderService service) {
        this.repo = repo;
        this.service = service;
    }

    /**
     * ADMIN: lista svih naloga
     * */
    @GetMapping
    public List<WorkOrderResponse> listAll(){
        return repo.findAll()
                .stream()
                .map(WorkOrderResponse::new)
                .toList();
    }

    /**
     * ADMIN: filtriranje po statusu
     * */
    @GetMapping("/status/{status}")
    public List<WorkOrderResponse> listByStatus(@PathVariable WorkOrderStatus status) {
        return repo.findAllByStatusOrderByRequestedAtDesc(status)
                .stream()
                .map(WorkOrderResponse::new)
                .toList();
    }
    /**
     * ADMIN: Planiranje odvoza
     * */
    @PatchMapping("/{id}/schedule")
    public WorkOrderResponse scheduled(@PathVariable Long id) {

        WorkOrder wo = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("WorkOrder not found"));

        wo.setStatus(WorkOrderStatus.SCHEDULED);
        wo.setScheduledFor(LocalDateTime.now().plusDays(1));

        return new WorkOrderResponse(repo.save(wo));
    }
    /**
     * ADMIN: oznaci kao COMPLETED
     * */
    @PatchMapping("/{id}/complete")
    public WorkOrderResponse complete(@PathVariable Long id) {

        WorkOrder wo = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("WorkOrder not found"));

        wo.setStatus(WorkOrderStatus.COMPLETED);
        wo.setCompletedAt(LocalDateTime.now());

        return new WorkOrderResponse(repo.save(wo));
    }

    /**
     * ADMIN: filtriranje radnih naloga prema više kriterija.
     *
     * Primjeri:
     * /api/admin/work-orders/filter?status=CREATED
     * /api/admin/work-orders/filter?wasteTypeCode=PLASTIC
     * /api/admin/work-orders/filter?city=Cakovec
     * /api/admin/work-orders/filter?userEmail=test@test.com
     * /api/admin/work-orders/filter?status=COMPLETED&city=Cakovec
     */
    @GetMapping("/filter")
    public List<WorkOrderResponse> filter(
            @RequestParam(required = false) WorkOrderStatus status,
            @RequestParam(required = false) String wasteTypeCode,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String userEmail
    ) {
        return service.filterAdmin(status, wasteTypeCode, city, userEmail)
                .stream()
                .map(WorkOrderResponse::new)
                .toList();
    }

}
