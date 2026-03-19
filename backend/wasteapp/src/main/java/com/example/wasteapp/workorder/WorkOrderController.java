package com.example.wasteapp.workorder;



import com.example.wasteapp.workorder.dto.WorkOrderCreateRequest;
import com.example.wasteapp.workorder.dto.WorkOrderResponse;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * KLASA: WorkOrderController
 * SVRHA: USER endpoint za radne naloge (kreiranje + pregled vlastitih)
 * */

@RestController
@RequestMapping("/api/work-orders")
public class WorkOrderController {

    private final WorkOrderService service;

    public WorkOrderController(WorkOrderService service) {
        this.service = service;
    }
    /**
     * USER: kreira novi radni nalog.
     * */
    @PostMapping
    public WorkOrderResponse create(Authentication auth, @Valid @RequestBody WorkOrderCreateRequest req) {
        String email = auth.getName(); // iz JWT-a
        return new WorkOrderResponse(service.createForUser(email, req));
    }

    /**
     * USER: lista mojih naloga.
     * */
    @GetMapping("/mine")
    public List<WorkOrderResponse> myOrders(Authentication auth){
        String email = auth.getName();
        return service.listMine(email).stream().map(WorkOrderResponse::new).toList();
    }

}
