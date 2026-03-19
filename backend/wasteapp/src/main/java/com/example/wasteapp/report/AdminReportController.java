package com.example.wasteapp.report;


import com.example.wasteapp.report.dto.WasteByCityReportRow;
import com.example.wasteapp.report.dto.WasteTypeReportRow;
import com.example.wasteapp.report.dto.WorkOrderStatusReportRow;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * CONTROLLER: AdminReportController
 * SVRHA: ADMIN endpoint za izvjesca.
 * */
@RestController
@RequestMapping("/api/admin/reports")
public class AdminReportController {

    private final ReportService reportService;

    public AdminReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Izvjesće: ukupna količina otpada po vrsti.
     * */
    @GetMapping("/waste-by-type")
    public List<WasteTypeReportRow> wasteByType(){
        return reportService.getTotalWastebyType();
    }

    /**
     * Izvješće: broj radnih naloga po statusu.
     * */
    @GetMapping("/work-orders-by-status")
    public List<WorkOrderStatusReportRow> workOrderByStatus() {
        return reportService.getWorkOrderCountByStatus();
    }

    /**
     * Izvješće: ukupna količina otpada po gradu.
     */
    @GetMapping("/waste-by-city")
    public List<WasteByCityReportRow> wasteByCity() {
        return reportService.getWasteByCity();
    }

}
