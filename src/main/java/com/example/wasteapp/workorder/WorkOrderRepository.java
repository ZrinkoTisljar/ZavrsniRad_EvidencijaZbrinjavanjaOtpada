package com.example.wasteapp.workorder;

import com.example.wasteapp.report.dto.WasteByCityReportRow;
import com.example.wasteapp.report.dto.WasteTypeReportRow;
import com.example.wasteapp.report.dto.WorkOrderStatusReportRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * INTERFACE: WorkOrderRepository
 * SVRHA: Upiti nad radnim nalozima.
 */
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {

    // USER: moji nalozi (sortirano najnoviji prvi)
    List<WorkOrder> findAllByUser_IdOrderByRequestedAtDesc(Long userId);

    // ADMIN: svi po statusu
    List<WorkOrder> findAllByStatusOrderByRequestedAtDesc(WorkOrderStatus status);

    // ---> NOVO: ADMIN: dohvati apsolutno sve naloge, ali od najnovijeg! <---
    List<WorkOrder> findAllByOrderByRequestedAtDesc();

    /**
     * ADMIN: filtriranje radnih naloga po više opcionalnih kriterija.
     *
     * PROGRAMSKI ENTITET: repository metoda s JPQL upitom
     * SVRHA:
     * - Ako je neki parametar null, taj kriterij se ignorira.
     * - Omogućuje fleksibilno filtriranje bez velikog broja zasebnih metoda.
     */
    @Query("""
        SELECT wo
        FROM WorkOrder wo
        WHERE (:status IS NULL OR wo.status = :status)
          AND (:wasteTypeCode IS NULL OR wo.wasteType.code = :wasteTypeCode)
          AND (:city IS NULL OR :city = '' OR LOWER(wo.pickupAddress) LIKE LOWER(CONCAT('%', :city, '%')))
          AND (:userEmail IS NULL OR LOWER(wo.user.email) = LOWER(:userEmail))
        ORDER BY wo.requestedAt DESC
    """)
    List<WorkOrder> filterAdmin(
            @Param("status") WorkOrderStatus status,
            @Param("wasteTypeCode") String wasteTypeCode,
            @Param("city") String city,
            @Param("userEmail") String userEmail
    );

    /**
     * IZVJEŠĆE:
     * Ukupna količina otpada po vrsti otpada.
     */
    @Query("""
        SELECT new com.example.wasteapp.report.dto.WasteTypeReportRow(
            wo.wasteType.code,
            wo.wasteType.name,
            SUM(wo.quantity)
        )
        FROM WorkOrder wo
        WHERE wo.status = com.example.wasteapp.workorder.WorkOrderStatus.COMPLETED
        GROUP BY wo.wasteType.code, wo.wasteType.name
        ORDER BY wo.wasteType.name ASC
    """)
    List<WasteTypeReportRow> reportTotalWasteByType();

    /**
     * IZVJEŠĆE:
     * Broj radnih naloga po statusu.
     */
    @Query("""
        SELECT new com.example.wasteapp.report.dto.WorkOrderStatusReportRow(
            wo.status,
            COUNT(wo)
        )
        FROM WorkOrder wo
        GROUP BY wo.status
        ORDER BY wo.status
    """)
    List<WorkOrderStatusReportRow> reportCountByStatus();

    /**
     * IZVJEŠĆE:
     * Ukupna količina otpada po gradu.
     */
    @Query("""
            SELECT new com.example.wasteapp.report.dto.WasteByCityReportRow(
            wo.collectionPoint.city,
            SUM(wo.quantity)
            )
            FROM WorkOrder wo
            WHERE wo.status = com.example.wasteapp.workorder.WorkOrderStatus.COMPLETED
            GROUP BY wo.collectionPoint.city
            ORDER BY wo.collectionPoint.city
            """)
    List<WasteByCityReportRow> reportTotalWasteByCity();

}