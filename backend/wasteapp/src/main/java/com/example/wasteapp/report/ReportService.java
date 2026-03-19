package com.example.wasteapp.report;


import com.example.wasteapp.report.dto.WasteByCityReportRow;
import com.example.wasteapp.report.dto.WasteTypeReportRow;
import com.example.wasteapp.report.dto.WorkOrderStatusReportRow;
import com.example.wasteapp.workorder.WorkOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * KLASA: ReportService
 * SVRHA: Poslovna logika za izvješća sustava.
 * */
@Service
public class ReportService {

    private final WorkOrderRepository workOrderRepository;

    public ReportService(WorkOrderRepository workOrderRepository) {
        this.workOrderRepository = workOrderRepository;
    }

    /**
     * IZVJEŠĆE:
     * Vraća ukupnu količinu otpada po vrsti.
     * */
    public List<WasteTypeReportRow> getTotalWastebyType() {
        return workOrderRepository.reportTotalWasteByType();
    }

    /**
     * IZVJEŠĆE:
     * Vraća broj naloga po statusu.
     * */
    public List<WorkOrderStatusReportRow> getWorkOrderCountByStatus(){
        return workOrderRepository.reportCountByStatus();
    }

    /***
     * IZVJEŠĆE:
     * Ukupna količina otpada po gradu.
     */
    public List<WasteByCityReportRow> getWasteByCity(){
        return workOrderRepository.reportTotalWasteByCity();
    }
}
