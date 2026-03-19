package com.example.wasteapp.workorder;

import com.example.wasteapp.reference.CollectionPoint;
import com.example.wasteapp.reference.WasteType;
import com.example.wasteapp.user.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * KLASA: WorkOrder (JPA Entity)
 * SVRHA: Radni nalog / zahtjev za zbrinjavanje otpada.
 * TABLICA: work_orders
 */
@Entity
@Table(name = "work_orders")
public class WorkOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * PROGRAMSKI ENTITET: ManyToOne relacija
     * SVRHA: Veza na korisnika koji je kreirao nalog.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Veza na šifrarnik vrsta otpada.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "waste_type_id", nullable = false)
    private WasteType wasteType;

    /**
     * Veza na lokaciju (collection point).
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_point_id", nullable = false)
    private CollectionPoint collectionPoint;

    /**
     * Količina otpada (DECIMAL u bazi -> BigDecimal u Javi).
     */
    @Column(name = "quantity", nullable = false, precision = 12, scale = 3)
    private BigDecimal quantity;

    /**
     * Jedinica mjere (KG/T/M3).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "unit", nullable = false, length = 10)
    private QuantityUnit unit;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private WorkOrderStatus status;

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    @Column(name = "scheduled_for")
    private LocalDateTime scheduledFor;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "note", length = 500)
    private String note;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public WorkOrder() {
        // PROGRAMSKI ENTITET: konstruktor
        // SVRHA: inicijalni datumi kod kreiranja entiteta
        this.createdAt = LocalDateTime.now();
        this.requestedAt = LocalDateTime.now();
        this.status = WorkOrderStatus.CREATED;
    }

    // --- GETTERI/SETTERI ---

    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public WasteType getWasteType() { return wasteType; }
    public void setWasteType(WasteType wasteType) { this.wasteType = wasteType; }

    public CollectionPoint getCollectionPoint() { return collectionPoint; }
    public void setCollectionPoint(CollectionPoint collectionPoint) { this.collectionPoint = collectionPoint; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public QuantityUnit getUnit() { return unit; }
    public void setUnit(QuantityUnit unit) { this.unit = unit; }

    public WorkOrderStatus getStatus() { return status; }
    public void setStatus(WorkOrderStatus status) { this.status = status; }

    public LocalDateTime getRequestedAt() { return requestedAt; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }

    public LocalDateTime getScheduledFor() { return scheduledFor; }
    public void setScheduledFor(LocalDateTime scheduledFor) { this.scheduledFor = scheduledFor; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}