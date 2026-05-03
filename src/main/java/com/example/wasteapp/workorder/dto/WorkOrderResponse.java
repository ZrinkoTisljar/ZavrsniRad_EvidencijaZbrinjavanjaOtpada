package com.example.wasteapp.workorder.dto;


import com.example.wasteapp.workorder.WorkOrder;
import com.example.wasteapp.workorder.WorkOrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *  DTO KLASA: WorkOrderResponse
 *  SVRHA: JSON odgovor za prikaz radnog naloga u SPA-u.
 * */

public class WorkOrderResponse {

    private String collectionPointCity;
    private String collectionPointName;
    private Long collectionPointId;


    private  Long id;

    private Long userId;
    private String userName; // NOVO
    private String userEmail; //novo

    // --- ADRESA ---
    private String pickupAddress; // NOVO da user unese gdje se pokupi otpad

    private Long wasteTypeId;
    private String wasteTypeCode;
    private String wasteTypeName;

    private BigDecimal quantity;
    private String unit;

    private WorkOrderStatus status;

    private LocalDateTime requestAt;
    private LocalDateTime scheduledFor;
    private LocalDateTime completedAt;

    private String note;

    public WorkOrderResponse(WorkOrder wo){
        this.id = wo.getId();

        //NOVO
        // --- MAPIRANJE KORISNIKA ---
        this.userId = wo.getUser().getId();
        this.userEmail = wo.getUser().getEmail();

        // Ako ima fullName (Građanin), uzmia to. Ako nema, uzima companyName (Tvrtka).
        if (wo.getUser().getFullName() != null && !wo.getUser().getFullName().isEmpty()) {
            this.userName = wo.getUser().getFullName();
        } else {
            this.userName = wo.getUser().getCompanyName();
        }       // NOVO

        // Mapiranje adrese koju je građanin upisao
        this.pickupAddress = wo.getPickupAddress();//novo

        this.wasteTypeId = wo.getWasteType().getId();
        this.wasteTypeCode = wo.getWasteType().getCode();
        this.wasteTypeName = wo.getWasteType().getName();

        if (wo.getCollectionPoint() !=null){
            this.collectionPointId = wo.getCollectionPoint().getId();
            this.collectionPointName = wo.getCollectionPoint().getName();
            this.collectionPointCity = wo.getCollectionPoint().getCity();

        }else{
            this.collectionPointId=null; //ako nema lokacije pošalji null Reactu
        }


        this.quantity = wo.getQuantity();
        this.unit = wo.getUnit().name();

        this.status = wo.getStatus();

        this.requestAt = wo.getRequestedAt();
        this.scheduledFor = wo.getScheduledFor();
        this.completedAt = wo.getCompletedAt();

        this.note = wo.getNote();

    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }

    public Long getWasteTypeId() { return wasteTypeId; }
    public String getWasteTypeCode() { return wasteTypeCode; }
    public String getWasteTypeName() { return wasteTypeName; }

    public Long getCollectionPointId() { return collectionPointId; }
    public String getCollectionPointName() { return collectionPointName; }
    public  String getCollectionPointCity() { return  collectionPointCity; }

    public BigDecimal getQuantity() { return  quantity; }
    public String getUnit() { return unit; }

    public WorkOrderStatus getStatus() { return status; }

    public LocalDateTime getRequestAt() {return requestAt; }
    public LocalDateTime getScheduledFor() { return scheduledFor;}
    public LocalDateTime getCompletedAt() { return completedAt; }

    public String getNote() { return note; }


    public String getUserName() { return userName; }   // NOVO
    public String getUserEmail() { return userEmail; }         // NOVO
    public String getPickupAddress() { return pickupAddress; } // NOVO


}
