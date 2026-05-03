package com.example.wasteapp.workorder;

import com.example.wasteapp.reference.CollectionPoint;
import com.example.wasteapp.reference.WasteType;
import com.example.wasteapp.user.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * KLASA: WorkOrder (JPA Entity)
 * SVRHA: Predstavlja radni nalog (ili zahtjev korisnika) za zbrinjavanje otpada.
 * TABLICA: work_orders
 * * ARHITEKTONSKA NAPOMENA:
 * Ovo je središnji entitet (agregat) oko kojeg se vrti cijeli poslovni proces aplikacije.
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
     * NAPOMENA: optional = false znači da nalog ne može postojati bez korisnika.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * SVRHA: Veza na šifrarnik vrsta otpada.
     * NAPOMENA: Svaki zahtjev mora imati točno definiranu vrstu otpada.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "waste_type_id", nullable = false)
    private WasteType wasteType;

    /**
     * SVRHA: Veza na lokaciju (reciklažno dvorište).
     * KLJUČNA IZMJENA: optional = true
     * Zašto? Zato što korisnik prilikom prijave ne zna kamo otpad ide.
     * Lokacija je prazna (NULL) dok ju administrator naknadno ne odredi i odobri nalog.
     */
    @ManyToOne(optional = true, fetch = FetchType.LAZY) // <-- PROMIJENJENO U TRUE
    @JoinColumn(name = "collection_point_id") // Uklonjeno nullable=false ako ga je bilo
    private CollectionPoint collectionPoint;

    /**
     * Količina otpada.
     * DECIMAL(12,3) u bazi -> BigDecimal u Javi kako ne bi gubili preciznost kod decimala.
     */
    @Column(name = "quantity", nullable = false, precision = 12, scale = 3)
    private BigDecimal quantity;

    /**
     * Jedinica mjere (KG, T, M3).
     * Sprema se kao običan tekst (STRING) u bazu, iako je u Javi Enum.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "unit", nullable = false, length = 10)
    private QuantityUnit unit;

    /**
     * Status naloga (CREATED, SCHEDULED, COMPLETED).
     * Kontrolira u kojoj je fazi životnog ciklusa ovaj zahtjev.
     */
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

    /**
     * SVRHA: Točna adresa ili opis lokacije gdje se otpad trenutno nalazi.
     * Ovo upisuje građanin.
     */
    @Column(name = "pickup_address")
    private String pickupAddress;

    /**
     * PROGRAMSKI ENTITET: Konstruktor bez parametara (Default constructor)
     * SVRHA: Kad god kreiramo novi zahtjev, sustav mu automatski dodjeljuje
     * trenutno vrijeme i postavlja mu status u 'CREATED' (Kreiran/Na čekanju).
     */
    public WorkOrder() {
        this.createdAt = LocalDateTime.now();
        this.requestedAt = LocalDateTime.now();
        this.status = WorkOrderStatus.CREATED;
    }

    // --- GETTERI I SETTERI ---
    // Služe za siguran pristup i mijenjanje privatnih varijabli (Enkapsulacija)

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

    public String getPickupAddress() { return pickupAddress; }
    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }
}